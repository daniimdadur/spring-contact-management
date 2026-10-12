package com.guvaren.contact_management.master.addresses.dto.entity;

import com.guvaren.contact_management.base.BaseAuditableSoftDelete;
import com.guvaren.contact_management.master.contacts.dto.entity.ContactEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "t_addresses")
public class AddressEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "country", length = 100, nullable = false)
    private String country;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    private ContactEntity contact;
}
