package com.guvaren.contact_management.master.contacts.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactRes {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
