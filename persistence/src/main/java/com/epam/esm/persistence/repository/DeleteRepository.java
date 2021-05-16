package com.epam.esm.persistence.repository;

/**
 * Interface for performing delete operation with entities from data source
 *
 * @param <T> object type to work with
 */
public interface DeleteRepository<T> extends ReadOperationRepository<T> {

    /**
     * Method for deleting object from data source
     *
     * @param id of object to be deleted by
     * @return amount of rows deleted from data source
     */
    void delete(Long id);
}
