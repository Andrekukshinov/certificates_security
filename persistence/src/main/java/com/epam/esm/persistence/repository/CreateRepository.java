package com.epam.esm.persistence.repository;

/**
 * Interface for performing delete operation with entities from data source
 *
 * @param <T> object type to work with
 */
public interface CreateRepository<T> {

    /**
     * Method for saving entity with specified data source
     *
     * @param t object to be saved
     * @return saved id
     */
    T save(T t);
}
