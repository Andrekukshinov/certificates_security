package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;
import com.epam.esm.persistence.model.specification.CertificateDescriptionSpecification;
import com.epam.esm.persistence.model.specification.CertificateNameSpecification;
import com.epam.esm.persistence.model.specification.CertificatesStatusSpecification;
import com.epam.esm.persistence.model.specification.FindAllActiveCertificatesSpecification;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.model.specification.GiftCertificateTagNamesSpecification;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.service.dto.certificate.GiftCertificateTagDto;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.GiftCertificateMapper;
import com.epam.esm.service.model.RequestParams;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService{

    private static final String WRONG_CERTIFICATE = "certificate with id = %d not found";

    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;
    private final GiftCertificateMapper certificateMapper;


    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository, TagService tagService, GiftCertificateMapper certificateMapper) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.certificateMapper = certificateMapper;
    }

    @Transactional
    @Override
    public GiftCertificateTagDto save(GiftCertificateTagDto certificateDto) throws ValidationException {
        GiftCertificate certificate = certificateMapper.map(certificateDto);
        saveCertificateTags(certificate);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setStatus(GiftCertificateStatus.ACTIVE);
        GiftCertificate saved = certificateRepository.save(certificate);
        return certificateMapper.map(saved);
    }

    @Override
    public GiftCertificateTagDto getCertificateById(Long id) {
        Optional<GiftCertificate> certificateOptional = getGiftCertificateOptional(id);
        GiftCertificate certificate = certificateOptional
                .orElseThrow(() -> new EntityNotFoundException(String.format(WRONG_CERTIFICATE, id)));
        return certificateMapper.map(certificate);
    }

    @Override
    @Transactional
    public GiftCertificateTagDto updateCertificate(GiftCertificateTagDto certificateDto, Long updateId) throws ValidationException {
        return updateById(certificateDto, updateId);
    }

    private GiftCertificateTagDto updateById(GiftCertificateTagDto certificateDto, Long updateId) throws ValidationException {
        GiftCertificate found = getGiftCertificateOptional(updateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WRONG_CERTIFICATE, updateId)));
        certificateMapper.map(certificateDto, found);
        found.setStatus(GiftCertificateStatus.ACTIVE);
        found.setLastUpdateDate(LocalDateTime.now());
        saveCertificateTags(found);
        GiftCertificate updated = certificateRepository.save(found);
        return certificateMapper.map(updated);
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
        Optional<GiftCertificate> optionalGiftCertificate = getGiftCertificateOptional(id);
        optionalGiftCertificate.ifPresentOrElse(
                giftCertificate -> certificateRepository.deleteById(id),
                () -> {
                    throw new EntityNotFoundException("certificate with id " + id + " does not exist");
                }
        );
    }

    @Override
    public Page<GiftCertificateTagDto> getBySpecification(RequestParams params, Pageable pageable) {
        Page<GiftCertificate> page = getCertificatesBySpecification(params, pageable);
        return page.map(certificateMapper::map);
    }

    @Override
    public Page<GiftCertificate> getCertificatesBySpecification(RequestParams params, Pageable pageable) {
        Specification<GiftCertificate> specification = getGiftCertificateSpecification(params);
        return certificateRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public GiftCertificateTagDto patchUpdate(Long certificateId, GiftCertificateTagDto toBeUpdated) throws ValidationException {
        return updateById(toBeUpdated, certificateId);
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

    private Optional<GiftCertificate> getGiftCertificateOptional(Long id) {
        Specification<GiftCertificate> activeCertificates
                = new CertificatesStatusSpecification(GiftCertificateStatus.ACTIVE);
        Specification<GiftCertificate> specification = activeCertificates.and(new FindByIdInSpecification<>(List.of(id)));
        GiftCertificate nullable = DataAccessUtils.singleResult(certificateRepository.findAll(specification));
        return Optional.ofNullable(nullable);
    }
}
