package com.mgrigorakis.schedulo.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
@Embeddable
public class BusinessMemberId implements Serializable {
    @Column(name = "user_id")
    private Long userId;;

    @Column(name = "business_id")
    private Long businessId;
}
