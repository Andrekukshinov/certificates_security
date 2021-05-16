package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.certificate.GiftCertificateTagDto;
import com.epam.esm.service.dto.certificate.GiftCertificatesNoTagDto;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.model.RequestParams;
import com.epam.esm.service.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GiftCertificateServiceImplTest {

    private static final LocalDateTime DATE = LocalDateTime.parse("2021-03-25T00:00:00");
    private static final String SPA = "spa";
    private static final String FAMILY_CERTIFICATE = "family certificate";

    private static final long CERTIFICATE_ID_DEFAULT_ID = 1L;
    private static final Tag PEOPLE_TAG = new Tag(CERTIFICATE_ID_DEFAULT_ID, "PEOPLE");
    private static final Set<Tag> TAGS = Set.of(PEOPLE_TAG);

    private static final TagDto PEOPLE_TAG_DTO = new TagDto(1L, "PEOPLE");
    private static final Set<TagDto> TAGS_DTO = Set.of(PEOPLE_TAG_DTO);

    private static final GiftCertificate FIRST = GiftCertificate.getBuilder()
            .setId(CERTIFICATE_ID_DEFAULT_ID)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(754))
            .setDuration(3)
            .setTags(TAGS)
            .build();

    private static final GiftCertificate EMPTY_TAGS_CERTIFICATE = GiftCertificate.getBuilder()
            .setId(CERTIFICATE_ID_DEFAULT_ID)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(754))
            .setDuration(3)
            .setTags(new HashSet<>())
            .build();

    private static final GiftCertificate NO_TAGS_CERTIFICATE = GiftCertificate.getBuilder()
            .setId(CERTIFICATE_ID_DEFAULT_ID)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(754))
            .setDuration(3)
            .build();

    private static final GiftCertificateTagDto DTO = GiftCertificateTagDto.getBuilder()
            .setId(CERTIFICATE_ID_DEFAULT_ID)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(754))
            .setDuration(3)
            .setTags(TAGS_DTO)
            .build();

    private static final GiftCertificatesNoTagDto NO_TAGS_DTO = GiftCertificatesNoTagDto.getBuilder()
            .setId(CERTIFICATE_ID_DEFAULT_ID)
            .setCreateDate(DATE)
            .setLastUpdateDate(DATE)
            .setName(SPA)
            .setDescription(FAMILY_CERTIFICATE)
            .setPrice(new BigDecimal(754))
            .setDuration(3)
            .build();

    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TagService tagCertificateService;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveShouldSaveCertificateWithTagsWhenEntityValid() throws ValidationException {
        when(modelMapper.map(DTO, GiftCertificate.class)).thenReturn(FIRST);
        when(modelMapper.map(FIRST, GiftCertificateTagDto.class)).thenReturn(DTO);
        when(certificateRepository.save(any()))
                .thenAnswer((object) -> object.getArgument(0, GiftCertificate.class));

        GiftCertificateTagDto actual = service.save(DTO);

        assertThat(actual, is(DTO));
        verify(tagCertificateService, times(1)).saveAll(any());
        verify(certificateRepository, times(1)).save(FIRST);
        verify(modelMapper, times(1)).map(DTO, GiftCertificate.class);
    }

    @Test
    void testGetCertificateByIdShouldReturnDtoObjectWhenFound() throws ValidationException {
        when(certificateRepository.findById(any())).thenReturn(Optional.of(FIRST));
        when(modelMapper.map(FIRST, GiftCertificateTagDto.class)).thenReturn(DTO);
        when(modelMapper.map(PEOPLE_TAG, Tag.class)).thenReturn(PEOPLE_TAG);
        when(tagCertificateService.saveAll(any())).thenReturn(TAGS);

        GiftCertificateTagDto found = service.getCertificateById(CERTIFICATE_ID_DEFAULT_ID);

        assertThat(found, is(DTO));

    }

    @Test
    void testGetCertificateWithTagsByIdShouldThrowEntityNotFoundExceptionWhenNotFound() {
        when(certificateRepository.findById(CERTIFICATE_ID_DEFAULT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> service.getCertificateById(CERTIFICATE_ID_DEFAULT_ID));

        assertThat(entityNotFoundException.getMessage(), is("certificate with id = 1 not found"));
    }

    @Test
    void testUpdateCertificateShouldInvokeSaveCertificateTagsWhenTagsFound() throws ValidationException {
        when(modelMapper.map(DTO, GiftCertificate.class)).thenReturn(FIRST);
        when(modelMapper.map(FIRST, GiftCertificateTagDto.class)).thenReturn(DTO);
        when(certificateRepository.update(any())).thenReturn(FIRST);
        when(certificateRepository.findById(any())).thenReturn(Optional.of(FIRST));

        GiftCertificateTagDto actual = service.updateCertificate(DTO, CERTIFICATE_ID_DEFAULT_ID);

        assertThat(actual, is(DTO));
        verify(certificateRepository, times(1)).update(FIRST);
        verify(modelMapper, times(1)).map(DTO, GiftCertificate.class);
    }


    @Test
    void testUpdateCertificateByIdShouldThrowEntityNotFoundExceptionWhenNotFound() {
        when(certificateRepository.findById(CERTIFICATE_ID_DEFAULT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> service.updateCertificate(DTO, 1L));

        assertThat(entityNotFoundException.getMessage(), is("certificate with id = 1 not found"));
    }

    @Test
    void testUpdateCertificateShouldInvokeDeleteCertificateTagsWhenTagsNotFound() throws ValidationException {
        when(certificateRepository.findById(any())).thenReturn(Optional.of(FIRST));
        when(modelMapper.map(DTO, GiftCertificate.class)).thenReturn(FIRST);
        when(modelMapper.map(FIRST, GiftCertificateTagDto.class)).thenReturn(DTO);
        when(certificateRepository.update(any())).thenReturn(FIRST);

        GiftCertificateTagDto actual = service.updateCertificate(DTO, CERTIFICATE_ID_DEFAULT_ID);

        assertThat(actual, is(DTO));
        verify(certificateRepository, times(1)).update(FIRST);
        verify(modelMapper, times(1)).map(DTO, GiftCertificate.class);
    }

    @Test
    void testGetBySpecificationShouldReturnListOfDtoEntitiesWhenFound() {
        Page<GiftCertificate> page = new PageImpl<>(List.of(FIRST), Pageable.unpaged(), 1);
        Page<GiftCertificateTagDto> expected = new PageImpl<>(List.of(DTO), Pageable.unpaged(), 1);
        RequestParams params = RequestParams.builder()
                .setCertificateName("e")
                .setCertificateDescription("e")
                .setTagNames(Set.of("ss"))
                .build();
        when(certificateRepository.findBySpecification(any(), any())).thenReturn(page);
        when(modelMapper.map(FIRST, GiftCertificateTagDto.class)).thenReturn(DTO);

        Page<GiftCertificateTagDto> actual = service.getBySpecification(params, Pageable.unpaged());

        assertThat(actual, is(expected));

    }

    @Test
    void testDeleteCertificateShouldInvokeMethods() {
        when(certificateRepository.findById(any())).thenReturn(Optional.of(FIRST));

        service.deleteCertificate(CERTIFICATE_ID_DEFAULT_ID);


        verify(certificateRepository, times(1)).delete(1L);
    }

    @Test
    void testDeleteCertificateShouldThrowExceptionWhenNotFound() {

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> service.deleteCertificate(CERTIFICATE_ID_DEFAULT_ID));

        assertThat(entityNotFoundException.getMessage(), is("certificate with id 1 does not exist"));
    }


    @Test
    void testPatchUpdateCertificateByIdShouldThrowEntityNotFoundExceptionWhenNotFound() {
        when(certificateRepository.findById(CERTIFICATE_ID_DEFAULT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> service.patchUpdate(1L, DTO));

        assertThat(entityNotFoundException.getMessage(), is("certificate with id = 1 not found"));
    }

    @Test
    void testPatchUpdateCertificateShouldInvokeDeleteCertificateTagsWhenTagsNotFound() throws ValidationException {
        when(certificateRepository.findById(any())).thenReturn(Optional.of(FIRST));
        when(modelMapper.map(DTO, GiftCertificate.class)).thenReturn(FIRST);
        when(modelMapper.map(FIRST, GiftCertificateTagDto.class)).thenReturn(DTO);
        when(certificateRepository.partialUpdate(any(), any())).thenReturn(FIRST);

        GiftCertificateTagDto actual = service.patchUpdate(CERTIFICATE_ID_DEFAULT_ID, DTO);

        assertThat(actual, is(DTO));
        verify(certificateRepository, times(1)).partialUpdate(CERTIFICATE_ID_DEFAULT_ID, FIRST);
        verify(modelMapper, times(1)).map(DTO, GiftCertificate.class);
    }


}
