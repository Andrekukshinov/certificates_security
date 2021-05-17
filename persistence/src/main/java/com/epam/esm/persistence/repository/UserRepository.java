package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Interface for executing operations with order entity within data source
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
