package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.config.TestConfiguration;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.FindAllActiveCertificatesSpecification;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = {TestConfiguration.class})
@ActiveProfiles("test")
@Transactional
class GiftCertificateRepositoryImplTest {
    private static final LocalDateTime DATE = LocalDateTime.parse("2021-03-25T00:00:00");
    private static final String SPA = "spa";
    private static final String FAMILY_CERTIFICATE = "family certificate";
    private static final String GYM = "gym";
    private static final String GYM_CERTIFICATE = "for boss of the gym";
    private static final String POOL = "pool";
    private static final String CONNECTION_POOL = "for better connection";
    private static final String CLUB = "club";
    private static final String LEATHER_MAN = "for leatherman";

    private static final Tag FOURTH_TAG = new Tag(1L, "leatherTime");
    private static final Tag FIFTH_TAG = new Tag(2L, "people");

    private static final Set<Tag> FIFTH_FOURTH_TAG = Set.of(FIFTH_TAG, FOURTH_TAG);

    private static final Set<Tag> FOURTH_FIFTH_TAG = Set.of(FOURTH_TAG, FIFTH_TAG);

    private static final GiftCertificate FOR_SAVING = GiftCertificate.getBuilder()
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(754))
            .setDuration(3)
            .build();

    private static final GiftCertificate FIRST = GiftCertificate.getBuilder()
            .setId(1L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setTags(Set.of(FOURTH_TAG))
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(754))
            .setDuration(3)
            .build();

    private static final GiftCertificate SECOND = GiftCertificate.getBuilder()
            .setId(2L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(GYM)
            .setDescription(GYM_CERTIFICATE)
            .setPrice(new BigDecimal(300))
            .setDuration(14)
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setTags(FIFTH_FOURTH_TAG)
            .build();

    private static final GiftCertificate THIRD = GiftCertificate.getBuilder()
            .setId(3L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(POOL)
            .setDescription(CONNECTION_POOL)
            .setPrice(new BigDecimal(354))
            .setDuration(23)
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setTags(Set.of(FIFTH_TAG))
            .build();

    private static final GiftCertificate FOURTH = GiftCertificate.getBuilder()
            .setId(4L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(CLUB)
            .setDescription(LEATHER_MAN)
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setPrice(new BigDecimal(150))
            .setDuration(9)
            .setTags(FIFTH_FOURTH_TAG)
            .build();

    private static final GiftCertificate FIFTH = GiftCertificate.getBuilder()
            .setId(5L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName("separate")
            .setDescription("test me")
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setPrice(new BigDecimal(150))
            .setDuration(9)
            .setTags(Set.of(FIFTH_TAG))
            .build();

    private static final GiftCertificate SIXTH = GiftCertificate.getBuilder()
            .setId(6L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName("rich")
            .setDescription(LEATHER_MAN)
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setPrice(new BigDecimal(10000))
            .setDuration(9)
            .setTags(Set.of(FIFTH_TAG))
            .build();

    private static final GiftCertificate SEVENTH = GiftCertificate.getBuilder()
            .setId(7L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName("poor")
            .setDescription(LEATHER_MAN)
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setPrice(new BigDecimal(1))
            .setDuration(9)
            .setTags(Set.of(FOURTH_TAG))
            .build();

    private static final GiftCertificate EIGHTH = GiftCertificate.getBuilder()
            .setId(8L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName("pic kme")
            .setDescription(LEATHER_MAN)
            .setStatus(GiftCertificateStatus.ACTIVE)
            .setPrice(new BigDecimal(12))
            .setDuration(9)
            .setTags(Set.of(FOURTH_TAG))
            .build();


    private static final GiftCertificate FOR_UPDATING = GiftCertificate.getBuilder()
            .setId(3L)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(755))
            .setDuration(3)
            .setTags(FOURTH_FIFTH_TAG)
            .build();

    private static final Pageable FIRST_THREE_CERTIFICATES_PAGEABLE = new Pageable(0, 3, "id", "ASC");
    private static final List<GiftCertificate> FIRST_THREE = List.of(FIRST, SECOND, THIRD);
    private static final Page<GiftCertificate> EXPECTED_PAGE_3_VALUES = new PageImpl<GiftCertificate>(FIRST_THREE, FIRST_THREE_CERTIFICATES_PAGEABLE, 8);

    private static final Pageable ALL_CERTIFICATES_PAGEABLE = new Pageable(0, 7, "id", "ASC");
    private static final List<GiftCertificate> ALL_ACTIVE = List.of(FIRST, SECOND, THIRD, FOURTH, SIXTH, SEVENTH, EIGHTH);
    private static final Page<GiftCertificate> EXPECTED_PAGE_ALL_ACTIVE = new PageImpl<GiftCertificate>(ALL_ACTIVE, ALL_CERTIFICATES_PAGEABLE, 8);

    @Autowired
    private GiftCertificateRepository repository;

    @Test
    @Rollback
    void testFindByIdShouldReturnObjectWhenFound() {
        Optional<GiftCertificate> certificateOptional = repository.findById(1L);

        GiftCertificate giftCertificate = certificateOptional.get();
        assertThat(giftCertificate, is(FIRST));
    }

    @Test
    @Rollback
    void testSaveShouldReturnObjectIdWhenSaved() {
        GiftCertificate saved = repository.save(FOR_SAVING);

        assertThat(saved, is(FOR_SAVING));
    }

    @Test
    @Rollback
    void testUpdateShouldReturnUpdatedObjectsAmountWhenUpdated() {
        GiftCertificate actual = repository.update(FOR_UPDATING);

        assertThat(actual, is(FOR_UPDATING));
    }

    @Test
    @Rollback
    void testFindBySpecificationShouldReturnListOf3Certificates() {
        List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        Page<GiftCertificate> page = repository.findBySpecification(new FindByIdInSpecification<>(ids), FIRST_THREE_CERTIFICATES_PAGEABLE);

        assertThat(page.getContent(), is(EXPECTED_PAGE_3_VALUES.getContent()));
    }

    @Test
    @Rollback
    void testFindBySpecificationShouldReturnListOfAllCertificates() {
        Page<GiftCertificate> page = repository.findBySpecification(new FindAllActiveCertificatesSpecification(), ALL_CERTIFICATES_PAGEABLE);

        assertThat(page.getContent(), is(EXPECTED_PAGE_ALL_ACTIVE.getContent()));
    }
}
