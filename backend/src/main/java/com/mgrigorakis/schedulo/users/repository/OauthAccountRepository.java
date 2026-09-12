package com.mgrigorakis.schedulo.users.repository;

import com.mgrigorakis.schedulo.users.enums.OauthProvider;
import com.mgrigorakis.schedulo.users.model.OauthAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthAccountRepository extends JpaRepository<OauthAccount, Long> {
    @EntityGraph(attributePaths = {"user", "user.platformRole"})
    Optional<OauthAccount> findByProviderAndExternalSubject(OauthProvider oauthProvider, String externalSubject);


    boolean existsByProviderAndExternalSubject(OauthProvider provider, String externalSubject);
}
