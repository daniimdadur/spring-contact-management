package com.guvaren.contact_management.master.auth.dto.req;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationReq {
    private String email;
    private String password;
}
