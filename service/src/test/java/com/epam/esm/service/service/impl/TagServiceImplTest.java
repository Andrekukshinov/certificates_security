package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.TagNameSpecification;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DeleteTagInUseException;
import com.epam.esm.service.exception.EntityAlreadyExistsException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
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

class TagServiceImplTest {
    private static final Long CERTIFICATE_ID_DEFAULT_ID = 1L;

    private static final Tag PEOPLE_TAG = new Tag(CERTIFICATE_ID_DEFAULT_ID, "PEOPLE");
    private static final Tag SECOND_PEOPLE_TAG = new Tag(2L, "PEOPLE");
    private static final Tag NO_ID_PEOPLE_TAG = new Tag(null, "PEOPLE");
    private static final Tag NO_ID_GYM_TAG = new Tag(null, "GYM");
    private static final Tag GYM_TAG = new Tag(2L, "GYM");
    private static final Set<Tag> TWO_TAGS_THE_SAME_NAME = Set.of(PEOPLE_TAG, SECOND_PEOPLE_TAG);
    private static final Set<Tag> TWO_TAGS = Set.of(NO_ID_PEOPLE_TAG, NO_ID_GYM_TAG);
    private static final Set<Tag> TAGS = Set.of(PEOPLE_TAG);

    private static final TagDto PEOPLE_TAG_DTO = new TagDto(1L, "PEOPLE");
    private static final TagDto SECOND_PEOPLE_TAG_DTO = new TagDto(2L, "PEOPLE");
    private static final Set<TagDto> TAGS_DTO = Set.of(PEOPLE_TAG_DTO);

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Validator<Tag> validator;

    @InjectMocks
    private TagServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTagShouldVerifyRepositoryCallWhenObjectValid() {
        when(tagRepository.find(any(), any())).thenReturn(new PageImpl<>(new ArrayList<>(), Pageable.unpaged(), 1));
        when(tagRepository.save(any())).thenReturn((PEOPLE_TAG));
        when(modelMapper.map(PEOPLE_TAG_DTO, Tag.class)).thenReturn((PEOPLE_TAG));
        when(modelMapper.map(PEOPLE_TAG, TagDto.class)).thenReturn((PEOPLE_TAG_DTO));

        TagDto saved = service.save(PEOPLE_TAG_DTO);

        assertThat(saved, is(PEOPLE_TAG_DTO));
        verify(tagRepository, times(1)).save(any());
        verify(tagRepository, times(1)).find(any(), any());
        verify(modelMapper, times(2)).map(any(), any());
    }

    @Test
    void testSaveTagShouldEntityAlreadyExistsExceptionWhenTagExists() {
        when(tagRepository.find(any(), any())).thenReturn(new PageImpl<>(List.of(PEOPLE_TAG), new Pageable(1, 1, null, null), 1));
        when(modelMapper.map(any(), any())).thenReturn((PEOPLE_TAG));
        when(modelMapper.map(any(), any())).thenReturn(PEOPLE_TAG);

        assertThrows(EntityAlreadyExistsException.class, () -> service.save(PEOPLE_TAG_DTO));

        verify(tagRepository, times(0)).save(any());
        verify(tagRepository, times(1)).find(any(), any());
        verify(modelMapper, times(1)).map(any(), any());
    }

    @Test
    void testDeleteTagShouldInvokeRepositoryIfNotInvokedWithCertificates() {
        when(tagRepository.findById(any())).thenReturn(Optional.of(PEOPLE_TAG));

        service.deleteTag(CERTIFICATE_ID_DEFAULT_ID);

        verify(tagRepository, times(1)).delete(any());
        verify(tagRepository, times(1)).findById(any());
        verify(tagRepository, times(1)).findInCertificates(any());
    }

    @Test
    void testDeleteTagShouldThrowExceptionWhenTagNotFound() {

        assertThrows(EntityNotFoundException.class, () -> service.deleteTag(CERTIFICATE_ID_DEFAULT_ID));

        verify(tagRepository, times(0)).delete(any());
        verify(tagRepository, times(1)).findById(any());
        verify(tagRepository, times(1)).findInCertificates(any());
    }

    @Test
    void testDeleteTagShouldThrowDeleteTagInUseExceptionIfInvokedWithCertificates() {
        when(tagRepository.findInCertificates(any())).thenReturn(Optional.of(PEOPLE_TAG));

        assertThrows(DeleteTagInUseException.class, () -> service.deleteTag(PEOPLE_TAG.getId()));

        verify(tagRepository, times(1)).findInCertificates(any());
    }

    @Test
    void testGetTagShouldReturnTagWhenFound() {
        when(tagRepository.findById(any())).thenReturn(Optional.of(PEOPLE_TAG));
        when(modelMapper.map(any(), any())).thenReturn(PEOPLE_TAG_DTO);

        TagDto tag = service.getTag(CERTIFICATE_ID_DEFAULT_ID);

        assertThat(tag, is(PEOPLE_TAG_DTO));
        verify(tagRepository, times(1)).findById(any());
        verify(modelMapper, times(1)).map(any(), any());
    }

    @Test
    void testGetTagShouldThrowEntityNotFoundExceptionWhenNotFound() {
        when(modelMapper.map(any(), any())).thenReturn(PEOPLE_TAG_DTO);

        assertThrows(EntityNotFoundException.class, () -> service.getTag(CERTIFICATE_ID_DEFAULT_ID));

        verify(tagRepository, times(1)).findById(any());
        verify(modelMapper, times(0)).map(any(), any());
    }

    @Test
    void testGetAllTagShouldReturnTagsPageWhenFound() {
        Pageable pageable = new Pageable(1, 1, null, null);
        when(tagRepository.find(any(), any())).thenReturn(new PageImpl<>(List.of(PEOPLE_TAG), pageable, 1));
        when(modelMapper.map(any(), any())).thenReturn(PEOPLE_TAG_DTO);
        PageImpl<TagDto> expectedPage = new PageImpl<>(List.of(PEOPLE_TAG_DTO), pageable, 1);

        Page<TagDto> tagDtoPage = service.getAll(pageable);

        assertThat(tagDtoPage, is(expectedPage));
        verify(tagRepository, times(1)).find(any(), any());
        verify(modelMapper, times(1)).map(any(), any());
    }

    @Test
    void testSaveAllShouldReturnSetTagsWhenByIdFound() throws ValidationException {
        when(tagRepository.findById(any())).thenAnswer((arg) ->
                Optional.of(new Tag(arg.getArgument(0), "PEOPLE"))
        );
        when(modelMapper.map(any(), any())).thenReturn(PEOPLE_TAG_DTO);

        Set<Tag> actual = service.saveAll(TWO_TAGS_THE_SAME_NAME);

        assertThat(actual, is(TWO_TAGS_THE_SAME_NAME));
        verify(tagRepository, times(2)).findById(any());
        verify(validator, times(2)).validate(any());
    }

    @Test
    void testSaveAllShouldReturnSetTagsWhenByNameNotFound() throws ValidationException {
        Page<Tag> peoplePage = new PageImpl<>(List.of(PEOPLE_TAG), Pageable.unpaged(), 1);
        Page<Tag> emptyPage = new PageImpl<>(List.of(), Pageable.unpaged(), 0);
        when(tagRepository.find(new TagNameSpecification("PEOPLE"), Pageable.unpaged())).thenReturn(peoplePage);
        when(tagRepository.find(new TagNameSpecification("GYM"), Pageable.unpaged())).thenReturn(emptyPage);
        when(tagRepository.save(NO_ID_GYM_TAG)).thenReturn(GYM_TAG);
        when(modelMapper.map(any(), any())).thenReturn(PEOPLE_TAG_DTO);

        Set<Tag> actual = service.saveAll(TWO_TAGS);

        assertThat(actual, is(Set.of(PEOPLE_TAG, GYM_TAG)));
        verify(tagRepository, times(0)).findById(any());
        verify(tagRepository, times(2)).find(any(), any());
        verify(tagRepository, times(1)).save(any());
        verify(validator, times(2)).validate(any());
    }


    @Test
    void testGetTagShouldThrowEntityNotFoundExceptionWhenNotFound2() {
        assertThrows(InvalidEntityException.class, () -> service.saveAll(TWO_TAGS_THE_SAME_NAME));

        verify(tagRepository, times(1)).findById(any());
    }

    @Test
    void testGetTopUserMostPopularTagShouldReturnTag() {
        when(tagRepository.getTopUserMostPopularTag()).thenReturn(PEOPLE_TAG);
        when(modelMapper.map(any(), any())).thenReturn(PEOPLE_TAG_DTO);

        TagDto actual = service.getTopUserMostPopularTag();

        assertThat(actual, is(PEOPLE_TAG_DTO));
        verify(tagRepository, times(1)).getTopUserMostPopularTag();
        verify(modelMapper, times(1)).map(any(), any());

    }
}
