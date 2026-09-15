package com.mgrigorakis.schedulo.business.repository;

import com.mgrigorakis.schedulo.business.model.BusinessRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRoleRepository extends JpaRepository<BusinessRole, Integer> {
}
