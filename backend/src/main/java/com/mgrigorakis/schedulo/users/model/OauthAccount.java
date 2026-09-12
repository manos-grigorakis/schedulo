package com.mgrigorakis.schedulo.users.model;

import com.mgrigorakis.schedulo.users.enums.OauthProvider;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "oauth_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_oauth_account_provider_external_subject",
                        columnNames = {"provider", "external_subject"}
                )
        }
)
@Entity
 public class OauthAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oauth_account_seq")
    @SequenceGenerator(name = "oauth_account_seq", sequenceName = "oauth_account_sequence", allocationSize = 20)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private OauthProvider provider;

    @Column(name = "external_subject", nullable = false)
    private String externalSubject;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public OauthAccount(OauthProvider provider, String externalSubject, User user) {
        this.provider = provider;
        this.externalSubject = externalSubject;
        this.user = user;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
