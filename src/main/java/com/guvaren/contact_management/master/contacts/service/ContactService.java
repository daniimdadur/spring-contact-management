package com.guvaren.contact_management.master.contacts.service;

import com.guvaren.contact_management.master.contacts.dto.req.ContactCreateReq;
import com.guvaren.contact_management.master.contacts.dto.req.ContactUpdateReq;
import com.guvaren.contact_management.master.contacts.dto.res.ContactRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {
    ContactRes create(ContactCreateReq req, String username);
    ContactRes update(ContactUpdateReq req, String id, String username);
    ContactRes get(String id, String username);
    Page<ContactRes> search(String name, String email, String phone, String username, Pageable pageable);
    String delete(String id, String username);
}
