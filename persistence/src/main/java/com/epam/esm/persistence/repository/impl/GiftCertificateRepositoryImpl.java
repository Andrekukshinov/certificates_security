package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;
import com.epam.esm.persistence.model.specification.CertificatesStatusSpecification;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Repository
@Transactional
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate> implements GiftCertificateRepository {
    private static final String ACTIVE_CERTIFICATE_CREATE_DATE = "SELECT GC.createDate from GiftCertificate GC WHERE GC.id = :id AND GC.status ='ACTIVE'";

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        Specification<GiftCertificate> byId = new FindByIdInSpecification<>(List.of(id));
        Specification<GiftCertificate> specification = byId.and(new CertificatesStatusSpecification(GiftCertificateStatus.ACTIVE));
        Page<GiftCertificate> certificate = findBySpecification(specification, Pageable.unpaged());
        GiftCertificate giftCertificate = DataAccessUtils.singleResult(certificate.getContent());
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public void delete(Long id) {
        findById(id).ifPresent(certificate -> {
            certificate.setStatus(GiftCertificateStatus.DELETED);
            manager.merge(certificate);
        });
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        Query query = manager.createQuery(ACTIVE_CERTIFICATE_CREATE_DATE)
                .setParameter("id", certificate.getId());
        LocalDateTime createDate = (LocalDateTime) query.getSingleResult();
        certificate.setCreateDate(createDate);
        manager.merge(certificate);
        return certificate;
    }

    @Override
    public GiftCertificate partialUpdate(Long certificateId, GiftCertificate toBeUpdated) {
        GiftCertificate found = manager.find(GiftCertificate.class, certificateId);
        setFieldsToUpdate(toBeUpdated, found);
        manager.merge(found);
        return found;
    }

    private void setFieldsToUpdate(GiftCertificate source, GiftCertificate target) {
        LocalDateTime createDate = source.getCreateDate();
        if (Objects.nonNull(createDate)) {
            target.setCreateDate(createDate);
        }
        LocalDateTime lastUpdateDate = source.getLastUpdateDate();
        if (Objects.nonNull(lastUpdateDate)) {
            target.setLastUpdateDate(lastUpdateDate);
        }
        Set<Tag> tags = source.getTags();
        if (Objects.nonNull(tags)) {
            target.setTags(tags);
        }
        String name = source.getName();
        if (Objects.nonNull(name)) {
            target.setName(name);
        }
        String description = source.getDescription();
        if (Objects.nonNull(description)) {
            target.setDescription(description);
        }
        BigDecimal price = source.getPrice();
        if (Objects.nonNull(price)) {
            target.setPrice(price);
        }
        GiftCertificateStatus status = source.getStatus();
        if (Objects.nonNull(status)) {
            target.setStatus(status);
        }
    }

    @Override
    protected Class<GiftCertificate> getEntityClass() {
        return GiftCertificate.class;
    }
}

