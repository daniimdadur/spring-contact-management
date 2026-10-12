package com.guvaren.contact_management.master.addresses.controller;

import com.guvaren.contact_management.base.Response;
import com.guvaren.contact_management.master.addresses.dto.req.AddressCreateReq;
import com.guvaren.contact_management.master.addresses.dto.req.AddressUpdateReq;
import com.guvaren.contact_management.master.addresses.dto.res.AddressRes;
import com.guvaren.contact_management.master.addresses.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts/{contactId}/addresses")
public class ApiAddressController {

    private final AddressService addressService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response<AddressRes>> create(
            @PathVariable String contactId,
            @Valid @RequestBody AddressCreateReq req,
            Authentication authentication) {
        AddressRes res = this.addressService.create(contactId, req, authentication.getName());
        return ResponseEntity.ok(Response.created(res));
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response<AddressRes>> update(
            @PathVariable String contactId,
            @PathVariable String addressId,
            @Valid @RequestBody AddressUpdateReq req,
            Authentication authentication) {
        AddressRes res = this.addressService.update(contactId, addressId, req, authentication.getName());
        return ResponseEntity.ok(Response.updated(res));
    }

    @GetMapping("/{addressId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response<AddressRes>> get(
            @PathVariable String contactId,
            @PathVariable String addressId,
            Authentication authentication) {
        AddressRes res = this.addressService.get(contactId, addressId, authentication.getName());
        return ResponseEntity.ok(Response.success(res));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response<List<AddressRes>>> list(
            @PathVariable String contactId,
            Authentication authentication) {
        List<AddressRes> res = this.addressService.list(contactId, authentication.getName());
        return ResponseEntity.ok(Response.success(res));
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response<String>> delete(
            @PathVariable String contactId,
            @PathVariable String addressId,
            Authentication authentication) {
        String res = this.addressService.delete(contactId, addressId, authentication.getName());
        return ResponseEntity.ok(Response.deleted(res));
    }
}
