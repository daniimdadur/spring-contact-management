package com.guvaren.contact_management.master.addresses.service.impl;

import com.guvaren.contact_management.exception.NotFoundException;
import com.guvaren.contact_management.master.addresses.dto.entity.AddressEntity;
import com.guvaren.contact_management.master.addresses.dto.req.AddressCreateReq;
import com.guvaren.contact_management.master.addresses.dto.req.AddressUpdateReq;
import com.guvaren.contact_management.master.addresses.dto.res.AddressRes;
import com.guvaren.contact_management.master.addresses.repository.AddressRepo;
import com.guvaren.contact_management.master.addresses.service.AddressService;
import com.guvaren.contact_management.master.auth.entity.UserEntity;
import com.guvaren.contact_management.master.auth.repository.UserRepo;
import com.guvaren.contact_management.master.contacts.dto.entity.ContactEntity;
import com.guvaren.contact_management.master.contacts.repository.ContactRepo;
import com.guvaren.contact_management.util.CommonUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final ContactRepo contactRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public AddressRes create(String contactId, AddressCreateReq req, String username) {
        ContactEntity contact = this.getOwnedContact(contactId, username);
        AddressEntity address = AddressEntity.builder()
                .id(CommonUtil.getUUID())
                .street(req.getStreet())
                .city(req.getCity())
                .province(req.getProvince())
                .country(req.getCountry())
                .postalCode(req.getPostalCode())
                .contact(contact)
                .build();
        return this.convertToAddressRes(this.addressRepo.save(address));
    }

    @Override
    @Transactional
    public AddressRes update(String contactId, String addressId, AddressUpdateReq req, String username) {
        this.getOwnedContact(contactId, username);
        AddressEntity address = this.addressRepo.findByIdAndContactId(addressId, contactId)
                .orElseThrow(() -> new NotFoundException("Address is not found"));
        address.setStreet(req.getStreet());
        address.setCity(req.getCity());
        address.setProvince(req.getProvince());
        address.setCountry(req.getCountry());
        address.setPostalCode(req.getPostalCode());
        return this.convertToAddressRes(this.addressRepo.save(address));
    }

    @Override
    public AddressRes get(String contactId, String addressId, String username) {
        this.getOwnedContact(contactId, username);
        AddressEntity address = this.addressRepo.findByIdAndContactId(addressId, contactId)
                .orElseThrow(() -> new NotFoundException("Address is not found"));
        return this.convertToAddressRes(address);
    }

    @Override
    public List<AddressRes> list(String contactId, String username) {
        this.getOwnedContact(contactId, username);
        return this.addressRepo.findAllByContactId(contactId).stream()
                .map(this::convertToAddressRes)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String delete(String contactId, String addressId, String username) {
        this.getOwnedContact(contactId, username);
        AddressEntity address = this.addressRepo.findByIdAndContactId(addressId, contactId)
                .orElseThrow(() -> new NotFoundException("Address is not found"));
        address.setDeletedAt(LocalDateTime.now());
        address.setDeletedBy(username);
        this.addressRepo.save(address);
        return "OK";
    }

    private UserEntity getUser(String username) {
        return this.userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private ContactEntity getOwnedContact(String contactId, String username) {
        UserEntity user = this.getUser(username);
        return this.contactRepo.findById(contactId)
                .filter(contact -> contact.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new NotFoundException("Contact is not found"));
    }

    private AddressRes convertToAddressRes(AddressEntity address) {
        return AddressRes.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }
}
