package com.guvaren.contact_management.master.contacts.dto.entity;

import com.guvaren.contact_management.base.BaseAuditableSoftDelete;
import com.guvaren.contact_management.master.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "t_contacts")
public class ContactEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "first_name", length = 64)
    private String firstName;

    @Column(name = "last_name", length = 64)
    private String lastName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 100)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "uid", nullable = false)
    private UserEntity user;
}
