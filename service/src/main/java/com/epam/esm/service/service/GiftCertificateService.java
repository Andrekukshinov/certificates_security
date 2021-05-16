package com.epam.esm.service.service;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.service.dto.certificate.GiftCertificateTagDto;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.model.RequestParams;


/**
 * Interface for performing business logics for GiftCertificates and GiftCertificatesDtos
 */
public interface GiftCertificateService {
    /**
     * Method that performs saving of certificateDto
     *
     * @param certificateDto dto to be validated and performed logics with
     * @return certificateDto dto saved
     * @throws ValidationException in case of validation error occur
     */
    GiftCertificateTagDto save(GiftCertificateTagDto certificateDto) throws ValidationException;

    /**
     * Method that returns GiftCertificateTag dto based on received id
     *
     * @param id to find object with
     * @return GiftCertificateTag dto entity with specified id
     * @throws com.epam.esm.service.exception.EntityNotFoundException if entity with id not exists
     */
    GiftCertificateTagDto getCertificateById(Long id);

    /**
     * Method that deletes certificate
     *
     * @param certificateId object id to perform logics with
     */
    void deleteCertificate(Long certificateId);

    /**
     * Method that performs update action
     *
     * @param certificateDto dto to be validated and performed logics with
     * @param updateId       certificate param to be updated by
     * @throws ValidationException in case of validation error occur
     */
    GiftCertificateTagDto updateCertificate(GiftCertificateTagDto certificateDto, Long updateId) throws ValidationException;

    /**
     * Method that returns list of GiftCertificateTag dto entities based on
     * received specification dto object
     *
     * @param params   to build search specification with
     * @param pageable to build page of certificates with
     * @return page with GiftCertificateTag dto entity
     * @throws com.epam.esm.service.exception.EntityNotFoundException if entity with id not exists
     */
    Page<GiftCertificateTagDto> getBySpecification(RequestParams params, Pageable pageable);

    /**
     * Internal usage only!
     * <p>
     * Method that returns list of GiftCertificate entities based on
     * received specification dto object
     *
     * @param params   to build search specification with
     * @param pageable to build page of certificates with
     * @return page with GiftCertificate entity
     * @throws com.epam.esm.service.exception.EntityNotFoundException if entity with id not exists
     */
    Page<GiftCertificate> getCertificatesBySpecification(RequestParams params, Pageable pageable);

    /**
     * Method that performs partly update action (from 1 field to complete object)
     *
     * @param toBeUpdated   dto to be validated and performed logics with
     * @param certificateId certificate param to be updated by
     * @throws ValidationException in case of validation error occur
     */
    GiftCertificateTagDto patchUpdate(Long certificateId, GiftCertificateTagDto toBeUpdated) throws ValidationException;
}
