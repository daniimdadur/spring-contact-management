package com.guvaren.contact_management.master.contacts.controller;

import com.guvaren.contact_management.base.PageResponse;
import com.guvaren.contact_management.base.Response;
import com.guvaren.contact_management.master.contacts.dto.req.ContactCreateReq;
import com.guvaren.contact_management.master.contacts.dto.req.ContactUpdateReq;
import com.guvaren.contact_management.master.contacts.dto.res.ContactRes;
import com.guvaren.contact_management.master.contacts.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ApiContactController {
    private final ContactService contactService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response<ContactRes>> create(@Valid @RequestBody ContactCreateReq req, Authentication authentication) {
        ContactRes res = this.contactService.create(req, authentication.getName());
        return ResponseEntity.ok(Response.created(res));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response<ContactRes>> update(@Valid @RequestBody ContactUpdateReq req, @PathVariable String id, Authentication authentication) {
        ContactRes res = this.contactService.update(req, id, authentication.getName());
        return ResponseEntity.ok(Response.updated(res));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response<ContactRes>> get(@PathVariable String id, Authentication authentication) {
        ContactRes res = this.contactService.get(id, authentication.getName());
        return ResponseEntity.ok(Response.success(res));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response<PageResponse<ContactRes>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.ASC
            ) Pageable pageable,
            Authentication authentication) {
        Page<ContactRes> result = this.contactService.search(name, email, phone, authentication.getName(), pageable);
        return ResponseEntity.ok(Response.successPage(result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response<String>> delete(@PathVariable String id, Authentication authentication) {
        String res = this.contactService.delete(id, authentication.getName());
        return ResponseEntity.ok(Response.deleted(res));
    }
}
