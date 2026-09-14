package com.mgrigorakis.schedulo.users.repository;

import com.mgrigorakis.schedulo.users.model.PlatformRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRoleRepository extends JpaRepository<PlatformRole, Long> {
    Optional<PlatformRole> findByName(String name);
}
