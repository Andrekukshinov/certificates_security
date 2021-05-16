package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.CertificateDescriptionSpecification;
import com.epam.esm.persistence.model.specification.CertificateNameSpecification;
import com.epam.esm.persistence.model.specification.CertificatesStatusSpecification;
import com.epam.esm.persistence.model.specification.FindAllActiveCertificatesSpecification;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.model.specification.GiftCertificateTagNamesSpecification;
import com.epam.esm.persistence.model.specification.Specification;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.service.dto.certificate.GiftCertificateTagDto;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.InvalidPageException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.model.RequestParams;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String WRONG_CERTIFICATE = "certificate with id = %d not found";

    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;
    private final ModelMapper modelMapper;


    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository, TagService tagService, ModelMapper modelMapper) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public GiftCertificateTagDto save(GiftCertificateTagDto certificateDto) throws ValidationException {
        GiftCertificate certificate = modelMapper.map(certificateDto, GiftCertificate.class);
        saveCertificateTags(certificate);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setStatus(GiftCertificateStatus.ACTIVE);
        GiftCertificate saved = certificateRepository.save(certificate);
        return modelMapper.map(saved, GiftCertificateTagDto.class);
    }

    @Override
    public GiftCertificateTagDto getCertificateById(Long id) {
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(id);
        GiftCertificate certificate = certificateOptional
                .orElseThrow(() -> new EntityNotFoundException(String.format(WRONG_CERTIFICATE, id)));
        return modelMapper.map(certificate, GiftCertificateTagDto.class);
    }

    @Override
    @Transactional
    public GiftCertificateTagDto updateCertificate(GiftCertificateTagDto certificateDto, Long updateId) throws ValidationException {
        certificateRepository.findById(updateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WRONG_CERTIFICATE, updateId)));
        certificateDto.setId(updateId);
        GiftCertificate certificate = modelMapper.map(certificateDto, GiftCertificate.class);
        certificate.setStatus(GiftCertificateStatus.ACTIVE);
        certificate.setLastUpdateDate(LocalDateTime.now());
        saveCertificateTags(certificate);
        GiftCertificate updated = certificateRepository.update(certificate);
        return modelMapper.map(updated, GiftCertificateTagDto.class);
    }

    private void saveCertificateTags(GiftCertificate certificate) throws ValidationException {
        Set<Tag> certificateTags = certificate.getTags();
        if (certificateTags != null && !certificateTags.isEmpty()) {
            Set<Tag> savedTags = tagService.saveAll(certificateTags);
            certificate.setTags(savedTags);
        }
    }

    @Override
    @Transactional
    public void deleteCertificate(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepository.findById(id);
        optionalGiftCertificate.ifPresentOrElse(
                giftCertificate -> certificateRepository.delete(id),
                () -> {
                    throw new EntityNotFoundException("certificate with id " + id + " does not exist");
                }
        );
    }

    @Override
    public Page<GiftCertificateTagDto> getBySpecification(RequestParams params, Pageable pageable) {
        Page<GiftCertificate> page = getCertificatesBySpecification(params, pageable);
        List<GiftCertificateTagDto> contentDto = page.getContent()
                .stream()
                .map(order -> modelMapper.map(order, GiftCertificateTagDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(contentDto, pageable, page.getLastPage());
    }

    @Override
    public Page<GiftCertificate> getCertificatesBySpecification(RequestParams params, Pageable pageable) {
        Specification<GiftCertificate> specification = getGiftCertificateSpecification(params);
        Page<GiftCertificate> page = certificateRepository.findBySpecification(specification, pageable);
        Integer lastPage = page.getLastPage();
        Integer currentPage = page.getPage();
        if (lastPage < currentPage){
            throw new InvalidPageException("current page: " + currentPage + " cannot be grater than last page: " + lastPage);
        }
        return page;
    }

    @Override
    @Transactional
    public GiftCertificateTagDto patchUpdate(Long certificateId, GiftCertificateTagDto toBeUpdated) throws ValidationException {
        certificateRepository.findById(certificateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WRONG_CERTIFICATE, certificateId)));
        GiftCertificate giftCertificate = modelMapper.map(toBeUpdated, GiftCertificate.class);
        saveCertificateTags(giftCertificate);
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        GiftCertificate updated = certificateRepository.partialUpdate(certificateId, giftCertificate);
        return modelMapper.map(updated, GiftCertificateTagDto.class);
    }

    private Specification<GiftCertificate> getGiftCertificateSpecification(RequestParams params) {
        List<Specification<GiftCertificate>> specifications = new ArrayList<>();
        List<Long> ids = params.getIds();
        Specification<GiftCertificate> activeCertificates
                = new CertificatesStatusSpecification(GiftCertificateStatus.ACTIVE);
        specifications.add(activeCertificates);
        String description = params.getCertificateDescription();
        String name = params.getCertificateName();
        Set<String> tagNames = params.getTagNames();
        if (!Objects.isNull(tagNames)) {
            tagNames.forEach(tagName -> {
                Specification<GiftCertificate> tagNamesSpecification = new GiftCertificateTagNamesSpecification(tagName);
                specifications.add(tagNamesSpecification);
            });
        }
        if (!Objects.isNull(description)) {
            Specification<GiftCertificate> descriptionSpecification = new CertificateDescriptionSpecification(description);
            specifications.add(descriptionSpecification);
        }
        if (!Objects.isNull(name)) {
            Specification<GiftCertificate> certificateNameSpecification = new CertificateNameSpecification(name);
            specifications.add(certificateNameSpecification);
        }
        if (!Objects.isNull(ids)) {
            Specification<GiftCertificate> idSpecification = new FindByIdInSpecification<>(ids);
            specifications.add(idSpecification);
        }
        return specifications
                .stream()
                .reduce(Specification::and)
                .orElse(new FindAllActiveCertificatesSpecification());
    }

}
