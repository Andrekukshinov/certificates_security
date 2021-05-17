package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepositoryImpl extends AbstractRepository<User> implements UserRepository {
    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
