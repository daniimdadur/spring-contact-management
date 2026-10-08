package com.guvaren.contact_management.master.auth.dto.req;

import com.guvaren.contact_management.master.auth.enums.Roles;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesReq {
    private Set<Roles> roles;
}
