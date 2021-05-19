package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Interface for executing operations with GiftCertificate entity within data source
 */
@Repository
@Transactional
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, JpaSpecificationExecutor<GiftCertificate> {
    /**
     * Method for updating certificate entity in the data source
     *
     * @param certificate object to be updated
     * @return amount of updated rows
     */
    @Query("update GiftCertificate gc set gc =:certificate where gc.id=:#{certificate.id}")
    GiftCertificate update(@Param("certificate") GiftCertificate certificate);

    @Modifying
    @Override
    @Query(value = ("UPDATE gift_certificates AS gc SET gc.status ='DELETED' WHERE gc.id =:id"), nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
