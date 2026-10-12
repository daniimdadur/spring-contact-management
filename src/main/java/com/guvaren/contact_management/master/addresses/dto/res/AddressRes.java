package com.guvaren.contact_management.master.addresses.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRes {
    private String id;
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;
}
