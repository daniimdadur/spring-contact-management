package com.guvaren.contact_management.master.contacts.repository;

import com.guvaren.contact_management.master.contacts.dto.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactRepo extends JpaRepository<ContactEntity, String>, JpaSpecificationExecutor<ContactEntity> {
}
