package com.guvaren.contact_management.master.auth.dto.res;

import com.guvaren.contact_management.master.auth.enums.Roles;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<Roles> roles;
}
