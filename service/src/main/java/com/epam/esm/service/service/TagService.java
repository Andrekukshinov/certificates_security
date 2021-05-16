package com.epam.esm.service.service;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ValidationException;

import java.util.Set;

/**
 * Interface for performing business logics for Tag and Tag Dtos
 */
public interface TagService {
    /**
     * Method that saving of tag
     *
     * @param tag dto to be performed logics with
     */
    TagDto save(TagDto tag);

    /**
     * Method that deletes object from system
     *
     * @param tagId object id to perform logics with
     */
    void deleteTag(Long tagId);

    /**
     * Method that returns tag dto based on received id
     *
     * @param id to find tag with
     * @return tag dto entity with specified id
     * @throws com.epam.esm.service.exception.EntityNotFoundException if entity with id not exists
     */
    TagDto getTag(Long id);

    /**
     * Method for returning list of all tag dtos that are present in the system
     *
     * @return list of tag dtos
     */
    Page<TagDto> getAll(Pageable pageable);

    /**
     * Method that saving of tags
     *
     * @param tagsToBeSaved dtos to be performed logics with
     */
    Set<Tag> saveAll(Set<Tag> tagsToBeSaved) throws ValidationException;

    /**
     * Method for retrieving tag that was the most widely used by user with the highest cost of all orders
     *
     * @return tag
     */
    TagDto getTopUserMostPopularTag();
}
