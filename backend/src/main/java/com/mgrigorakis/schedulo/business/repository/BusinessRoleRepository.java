package com.mgrigorakis.schedulo.business.repository;

import com.mgrigorakis.schedulo.business.model.BusinessRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BusinessRoleRepository extends JpaRepository<BusinessRole, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<BusinessRole> findByName(String name);
}
