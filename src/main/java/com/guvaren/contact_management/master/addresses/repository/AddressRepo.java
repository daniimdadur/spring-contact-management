package com.guvaren.contact_management.master.addresses.repository;

import com.guvaren.contact_management.master.addresses.dto.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<AddressEntity, String> {
    List<AddressEntity> findAllByContactId(String contactId);
    Optional<AddressEntity> findByIdAndContactId(String id, String contactId);
}
