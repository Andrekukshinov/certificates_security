package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String> {
}
