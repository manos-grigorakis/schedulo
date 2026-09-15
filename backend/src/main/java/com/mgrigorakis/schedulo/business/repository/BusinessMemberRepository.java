package com.mgrigorakis.schedulo.business.repository;

import com.mgrigorakis.schedulo.business.model.BusinessMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {
}
