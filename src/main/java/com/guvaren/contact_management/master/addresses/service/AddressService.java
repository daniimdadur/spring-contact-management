package com.guvaren.contact_management.master.addresses.service;

import com.guvaren.contact_management.master.addresses.dto.req.AddressCreateReq;
import com.guvaren.contact_management.master.addresses.dto.req.AddressUpdateReq;
import com.guvaren.contact_management.master.addresses.dto.res.AddressRes;

import java.util.List;

public interface AddressService {
    AddressRes create(String contactId, AddressCreateReq req, String username);
    AddressRes update(String contactId, String addressId, AddressUpdateReq req, String username);
    AddressRes get(String contactId, String addressId, String username);
    List<AddressRes> list(String contactId, String username);
    String delete(String contactId, String addressId, String username);
}
