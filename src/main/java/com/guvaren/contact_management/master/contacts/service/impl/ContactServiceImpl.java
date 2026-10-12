package com.guvaren.contact_management.master.contacts.service.impl;

import com.guvaren.contact_management.exception.NotFoundException;
import com.guvaren.contact_management.master.auth.entity.UserEntity;
import com.guvaren.contact_management.master.auth.repository.UserRepo;
import com.guvaren.contact_management.master.contacts.dto.entity.ContactEntity;
import com.guvaren.contact_management.master.contacts.dto.req.ContactCreateReq;
import com.guvaren.contact_management.master.contacts.dto.req.ContactUpdateReq;
import com.guvaren.contact_management.master.contacts.dto.res.ContactRes;
import com.guvaren.contact_management.master.contacts.repository.ContactRepo;
import com.guvaren.contact_management.master.contacts.service.ContactService;
import com.guvaren.contact_management.util.CommonUtil;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final ContactRepo contactRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public ContactRes create(ContactCreateReq req, String username) {
        UserEntity user = this.getUser(username);
        ContactEntity contact = ContactEntity.builder()
                .id(CommonUtil.getUUID())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .user(user)
                .build();
        return this.convertToContactRes(this.contactRepo.save(contact));
    }

    @Override
    @Transactional
    public ContactRes update(ContactUpdateReq req, String id, String username) {
        ContactEntity contact = this.getOwnedContact(id, username);
        contact.setFirstName(req.getFirstName());
        contact.setLastName(req.getLastName());
        contact.setEmail(req.getEmail());
        contact.setPhone(req.getPhone());
        return this.convertToContactRes(this.contactRepo.save(contact));
    }

    @Override
    public ContactRes get(String id, String username) {
        return this.convertToContactRes(this.getOwnedContact(id, username));
    }

    @Override
    public Page<ContactRes> search(String name, String email, String phone, String username, Pageable pageable) {
        UserEntity user = this.getUser(username);
        Specification<ContactEntity> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), user.getId()));

            if (name != null && !name.isBlank()) {
                String pattern = "%" + name.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern)
                ));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (phone != null && !phone.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return this.contactRepo.findAll(specification, pageable).map(this::convertToContactRes);
    }

    @Override
    @Transactional
    public String delete(String id, String username) {
        ContactEntity contact = this.getOwnedContact(id, username);
        contact.setDeletedAt(LocalDateTime.now());
        contact.setDeletedBy(username);
        this.contactRepo.save(contact);
        return "OK";
    }

    private UserEntity getUser(String username) {
        return this.userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private ContactEntity getOwnedContact(String id, String username) {
        UserEntity user = this.getUser(username);
        return this.contactRepo.findById(id)
                .filter(contact -> contact.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new NotFoundException("Contact is not found"));
    }

    private ContactRes convertToContactRes(ContactEntity contact) {
        return ContactRes.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
