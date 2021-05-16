package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.OrderCertificate;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.model.specification.FindUserOrdersSpecification;
import com.epam.esm.persistence.model.specification.OrderByUserIdSpecification;
import com.epam.esm.persistence.model.specification.Specification;
import com.epam.esm.persistence.repository.OrderRepository;
import com.epam.esm.service.dto.certificate.GiftCertificatesNoTagDto;
import com.epam.esm.service.dto.order.OrderCertificateUnitDto;
import com.epam.esm.service.dto.order.OrderCertificatesDto;
import com.epam.esm.service.dto.order.OrderDetailsDto;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.InvalidPageException;
import com.epam.esm.service.model.RequestParams;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String NOT_FOUND = "order with id = %s not found";
    private static final String NOT_FOUND_PATTERN = "Certificate with id = %s not found";
    private final GiftCertificateService certificateService;
    private final ModelMapper mapper;
    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(GiftCertificateService certificateService, ModelMapper mapper, OrderRepository repository) {
        this.certificateService = certificateService;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    @Transactional
    public OrderCertificatesDto save(OrderCertificatesDto orderCertificatesDto) {
        Set<OrderCertificateUnitDto> orderCertificates = orderCertificatesDto.getOrderCertificates();
        List<Long> ids = getCertificatesIds(orderCertificates);
        List<OrderCertificate> toBeSaved = getOrderCertificatesWithExistingCertificates(orderCertificates, ids);

        Order saved = saveOrder(orderCertificatesDto, toBeSaved);
        return mapper.map(saved, OrderCertificatesDto.class);
    }

    private List<OrderCertificate> getOrderCertificatesWithExistingCertificates(Set<OrderCertificateUnitDto> orderCertificates, List<Long> ids) {
        List<GiftCertificate> foundCertificates = getFoundCertificates(ids);
        Map<Long, GiftCertificate> idAndCertificate = new HashMap<>();
        foundCertificates.forEach(certificate -> idAndCertificate.put(certificate.getId(), certificate));
        validateCertificates(orderCertificates, idAndCertificate);
        return orderCertificates
                .stream()
                .map(orderCertificateUnitDto ->
                        mapper.map(orderCertificateUnitDto, OrderCertificate.class))
                .peek(orderCertificate -> {
                    Long id = orderCertificate.getOrderCertificate().getId();
                    orderCertificate.setOrderCertificate(idAndCertificate.get(id));
                })
                .collect(Collectors.toList());
    }

    private void validateCertificates(Set<OrderCertificateUnitDto> orderedCertificates, Map<Long, GiftCertificate> foundIdAndCertificate) {
        if (foundIdAndCertificate.size() != orderedCertificates.size()) {
            Set<GiftCertificatesNoTagDto> certificates = orderedCertificates
                    .stream()
                    .map(OrderCertificateUnitDto::getOrderCertificate)
                    .collect(Collectors.toSet());
            certificates
                    .stream()
                    .filter(certificate -> foundIdAndCertificate.get(certificate.getId()) == null)
                    .findFirst()
                    .ifPresent(certificate -> {
                        throw new EntityNotFoundException(String.format(NOT_FOUND_PATTERN, certificate.getId()));
                    });
        }
    }

    private List<Long> getCertificatesIds(Set<OrderCertificateUnitDto> orderCertificates) {
        return orderCertificates
                .stream()
                .map(OrderCertificateUnitDto::getOrderCertificate)
                .map(GiftCertificatesNoTagDto::getId)
                .collect(Collectors.toList());
    }

    private Order saveOrder(OrderCertificatesDto orderCertificatesDto, List<OrderCertificate> toBeSaved) {
        Order order = mapper.map(orderCertificatesDto, Order.class);
        order.setOrderCertificates(toBeSaved);
        order.setCreateDate(LocalDateTime.now());
        setTotalPriceOrZero(order);
        order.getOrderCertificates()
                .forEach(orderCertificate -> orderCertificate.setOrder(order));
        return repository.save(order);
    }

    private void setTotalPriceOrZero(Order order) {
        List<OrderCertificate> foundCertificates = order.getOrderCertificates();
        Optional<BigDecimal> totalPriceOptional = foundCertificates
                .stream()
                .map(orderCertificate -> {
                    Integer quantity = orderCertificate.getQuantity();
                    BigDecimal price = orderCertificate.getOrderCertificate().getPrice();
                    return price.multiply(new BigDecimal(quantity));
                })
                .reduce(BigDecimal::add);
        BigDecimal totalPrice = totalPriceOptional.orElse(BigDecimal.ZERO);
        order.setTotalPrice(totalPrice);
    }

    private List<GiftCertificate> getFoundCertificates(List<Long> ids) {
        RequestParams requestParams = RequestParams
                .builder()
                .setIds(ids)
                .build();
        return certificateService.getCertificatesBySpecification(requestParams, Pageable.unpaged()).getContent();
    }

    @Override
    public OrderCertificatesDto getUserOrderById(Long userId, Long orderId) {
        Specification<Order> specification = Specification.and(new OrderByUserIdSpecification(userId), new FindByIdInSpecification<>(List.of(orderId)));
        Order nullableValue = DataAccessUtils.singleResult(repository.find(specification, Pageable.unpaged()).getContent());
        Optional<Order> orderOptional = Optional.ofNullable(nullableValue);
        Order order = orderOptional.orElseThrow(() -> new EntityNotFoundException(String.format("order (id=%s), of user (id=%s), bot found", orderId, userId)));
        return mapper.map(order, OrderCertificatesDto.class);
    }

    @Override
    public Page<OrderDetailsDto> getAllUserOrders(Long userId, Pageable pageable) {
        Specification<Order> getAllSpec = new FindUserOrdersSpecification(userId);
        Page<Order> ordersPage = repository.find(getAllSpec, pageable);
        List<Order> content = ordersPage.getContent();
        List<OrderDetailsDto> contentDto = content
                .stream()
                .map(order -> mapper.map(order, OrderDetailsDto.class))
                .collect(Collectors.toList());
        PageImpl<OrderDetailsDto> page = new PageImpl<>(contentDto, pageable, ordersPage.getLastPage());
        Integer lastPage = page.getLastPage();
        Integer currentPage = page.getPage();
        if (lastPage < currentPage){
            throw new InvalidPageException("current page: " + currentPage + " cannot be grater than last page: " + lastPage);
        }
        return page;
    }
}
