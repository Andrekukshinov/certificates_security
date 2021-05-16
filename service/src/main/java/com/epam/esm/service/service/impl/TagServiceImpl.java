package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.FindAllSpecification;
import com.epam.esm.persistence.model.specification.TagNameSpecification;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DeleteTagInUseException;
import com.epam.esm.service.exception.EntityAlreadyExistsException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidPageException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private static final String WRONG_TAG = "tag with id = %d not found";
    private static final String ALREADY_EXISTS_PATTERN = "tag with name %s already exists!";
    private static final String TAG_INVOLVED_MESSAGE = "Tag with id = %s is involved with certificates!";

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final Validator<Tag> validator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper, Validator<Tag> validator) {
        this.tagRepository = tagRepository;

        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public TagDto save(TagDto tagDto) {
        Tag tag = modelMapper.map(tagDto, Tag.class);
        String name = tag.getName();
        Optional<Tag> tagOptional = getTagFromRepo(name);
        tagOptional.ifPresent((ignored) -> {
            throw new EntityAlreadyExistsException(String.format(ALREADY_EXISTS_PATTERN, name));
        });
        Tag saved = tagRepository.save(tag);
        return modelMapper.map(saved, TagDto.class);
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        Optional<Tag> forDeleting = tagRepository.findInCertificates(tagId);
        forDeleting.ifPresentOrElse(
                tag -> {
                    throw new DeleteTagInUseException(String.format(TAG_INVOLVED_MESSAGE, tagId));
                },
                () -> {
                    deleteTagFromRepository(tagId);
                }
        );
    }

    private void deleteTagFromRepository(Long tagId) {
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        tagOptional.ifPresentOrElse(
                tag -> tagRepository.delete(tagId),
                () -> {throw new EntityNotFoundException(String.format(WRONG_TAG, tagId));}
        );
    }

    @Override
    public TagDto getTag(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        Tag tag = tagOptional.orElseThrow(() -> new EntityNotFoundException(String.format(WRONG_TAG, id)));
        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public Page<TagDto> getAll(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.find(new FindAllSpecification<>(), pageable);
        List<TagDto> contentDto = tagPage.getContent().stream()
                .map(order -> modelMapper.map(order, TagDto.class))
                .collect(Collectors.toList());
        PageImpl<TagDto> page = new PageImpl<>(contentDto, pageable, tagPage.getLastPage());
        Integer lastPage = page.getLastPage();
        Integer currentPage = page.getPage();
        if (lastPage < currentPage){
            throw new InvalidPageException("current page: " + currentPage + " cannot be grater than last page: " + lastPage);
        }
        return page;
    }

    @Override
    @Transactional
    public Set<Tag> saveAll(Set<Tag> tagsToBeSaved) throws ValidationException {
        Set<Tag> result = new HashSet<>();
        for (Tag tag : tagsToBeSaved) {
            validator.validate(tag);
            addSavedTag(result, tag);
        }
        return result;
    }

    private void addSavedTag(Set<Tag> result, Tag tag) {
        Long tagId = tag.getId();
        String name = tag.getName();
        if (tagId != null) {
            Optional<Tag> optionalTag = tagRepository.findById(tagId);
            optionalTag.ifPresentOrElse(
                    result::add,
                    () -> {
                        throw new InvalidEntityException("invalid tag with id = " + tagId);
                    }
            );
        } else {
            Optional<Tag> tagOptional = getTagFromRepo(name);
            tagOptional.ifPresentOrElse(
                    result::add,
                    () -> {
                        Tag saved = tagRepository.save(tag);
                        result.add(saved);
                    }
            );
        }
    }

    private Optional<Tag> getTagFromRepo(String name) {
        Page<Tag> tagPage = tagRepository.find(new TagNameSpecification(name), Pageable.unpaged());
        return Optional.ofNullable(DataAccessUtils.singleResult(tagPage.getContent()));
    }

    @Override
    public TagDto getTopUserMostPopularTag() {
        return modelMapper.map(tagRepository.getTopUserMostPopularTag(), TagDto.class);
    }
}
