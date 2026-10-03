package com.mgrigorakis.schedulo.business.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "businesses")
@Entity
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_seq")
    @SequenceGenerator(name = "business_seq", sequenceName = "business_sequence", allocationSize = 20)
    private Long id;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "slug", unique = true, nullable = false, length = 160)
    private String slug;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "logo_key")
    private String logoKey;

    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Column(name = "street", nullable = false, length = 30)
    private String street;

    @Column(name = "street_number", nullable = false, length = 10)
    private String streetNumber;

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "country", nullable = false, length = 40)
    private String country;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Business(String name, String slug, String email, String logoKey, String phone, String street,
                    String streetNumber, String postalCode, String city, String country) {
        this.name = name;
        this.slug = slug;
        this.email = email;
        this.logoKey = logoKey;
        this.phone = phone;
        this.street = street;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Builds the slug of the business based on the name and city
     *
     * @return The generated business slug
     */
    public String buildSlug() {
        return name.toLowerCase().replace(" ", "-") + "-" + city.toLowerCase();
    }
}
