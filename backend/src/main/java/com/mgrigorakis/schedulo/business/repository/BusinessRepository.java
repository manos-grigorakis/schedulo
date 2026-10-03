package com.mgrigorakis.schedulo.business.repository;

import com.mgrigorakis.schedulo.business.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findBusinessBySlug(String slug);
}
