package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.OrderCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.repository.OrderRepository;
import com.epam.esm.service.dto.certificate.GiftCertificatesNoTagDto;
import com.epam.esm.service.dto.order.OrderCertificateUnitDto;
import com.epam.esm.service.dto.order.OrderCertificatesDto;
import com.epam.esm.service.dto.order.OrderDetailsDto;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.service.GiftCertificateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {
    private static final LocalDateTime CREATE_DATE = LocalDateTime.of(2021, 5, 5, 12, 0);

    private static final GiftCertificate FOUND_CERTIFICATE = GiftCertificate.getBuilder()
            .setId(1L)
            .setCreateDate(CREATE_DATE)
            .setLastUpdateDate(CREATE_DATE)
            .setDescription("desc")
            .setDuration(1)
            .setName("name")
            .setPrice(new BigDecimal(150))
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setTags(Set.of(new Tag(1L, "tName")))
            .build();

    private static final GiftCertificatesNoTagDto CERTIFICATE_ID_ONLY = GiftCertificatesNoTagDto
            .getBuilder()
            .setId(1L)
            .build();
    private static final OrderCertificateUnitDto CERTIFICATE_AMOUNT_DTO = new OrderCertificateUnitDto(null, CERTIFICATE_ID_ONLY, 2);

    private static final OrderCertificatesDto ORDER_DTO =
            OrderCertificatesDto.builder()
                    .setId(1L)
                    .setCreateDate(CREATE_DATE)
                    .setUserId(1L)
                    .setTotalPrice(new BigDecimal(300))
                    .setOrderCertificates(Set.of(CERTIFICATE_AMOUNT_DTO))
                    .build();

    private static final Order ORDER =
            Order.builder()
                    .setId(1L)
                    .setCreateDate(CREATE_DATE)
                    .setTotalPrice(new BigDecimal(300))
                    .build();

    @Mock
    private GiftCertificateService certificateService;

    @Mock
    private OrderRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveShouldSaveOrderWhenAllCertificatesFound() {
        User user = new User();
        user.setId(1L);
        OrderCertificate orderCertificate = new OrderCertificate(null, null, FOUND_CERTIFICATE, CERTIFICATE_AMOUNT_DTO.getQuantity());
        Order order = Order.builder()
                .setId(1L)
                .setCreateDate(CREATE_DATE)
                .setUser(user)
                .setOrderCertificates(List.of(orderCertificate))
                .build();
        Order saved = Order.builder()
                .setId(1L)
                .setCreateDate(CREATE_DATE)
                .setUser(user)
                .setTotalPrice(new BigDecimal(300))
                .build();
        OrderCertificate savedCertificate = new OrderCertificate(null, saved, FOUND_CERTIFICATE, CERTIFICATE_AMOUNT_DTO.getQuantity());
        saved.setOrderCertificates(List.of(savedCertificate));
        Page<GiftCertificate> page = new PageImpl<>(List.of(FOUND_CERTIFICATE), Pageable.unpaged(), 1);
        when(certificateService.getCertificatesBySpecification(any(), any())).thenReturn(page);
        when(modelMapper.map(CERTIFICATE_AMOUNT_DTO, OrderCertificate.class)).thenReturn(orderCertificate);
        when(modelMapper.map(ORDER_DTO, Order.class)).thenReturn(order);
        when(modelMapper.map(saved, OrderCertificatesDto.class)).thenReturn(ORDER_DTO);
        when(repository.save(any())).thenReturn(saved);

        OrderCertificatesDto actual = orderService.save(ORDER_DTO);

        assertThat(actual, is(ORDER_DTO));
    }

    @Test
    void testGetUserOrderByIdShouldReturnUserOrderWhenFound() {
        Page<Order> page = new PageImpl<>(List.of(ORDER), Pageable.unpaged(), 1);
        when(repository.find(any(), any())).thenReturn(page);
        when(modelMapper.map(ORDER, OrderCertificatesDto.class)).thenReturn(ORDER_DTO);

        OrderCertificatesDto actual = orderService.getUserOrderById(1L, 1L);

        assertThat(actual, is(ORDER_DTO));
    }

    @Test
    void testGetUserOrderByIdShouldThrowEntityNotFoundExceptionWhenNotFound() {
        Page<Order> page = new PageImpl<>(new ArrayList<>(), Pageable.unpaged(), 1);
        when(repository.find(any(), any())).thenReturn(page);
        when(modelMapper.map(ORDER, OrderCertificatesDto.class)).thenReturn(ORDER_DTO);

        assertThrows(EntityNotFoundException.class, () -> orderService.getUserOrderById(1L, 1L));

    }

    @Test
    void testGetAllUserOrderByIdShouldReturnUserOrdersWhenFound() {

        OrderDetailsDto detailsDto = new OrderDetailsDto(1L, CREATE_DATE, new BigDecimal(500));
        Page<Order> page = new PageImpl<>(List.of(ORDER, ORDER), Pageable.unpaged(), 1);
        when(repository.find(any(), any())).thenReturn(page);
        when(modelMapper.map(ORDER, OrderDetailsDto.class)).thenReturn(detailsDto);
        Page<OrderDetailsDto> expected = new PageImpl<>(List.of(detailsDto, detailsDto), Pageable.unpaged(), 1);


        Page<OrderDetailsDto> actual = orderService.getAllUserOrders(1L, Pageable.unpaged());

        assertThat(actual, is(expected));
    }

}
