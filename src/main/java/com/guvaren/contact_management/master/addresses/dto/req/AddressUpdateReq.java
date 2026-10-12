package com.guvaren.contact_management.master.addresses.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateReq {

    @Size(max = 100)
    private String street;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String province;

    @NotBlank(message = "Country is required")
    @Size(max = 100)
    private String country;

    @Size(max = 10)
    private String postalCode;
}
