package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.Contact;
import com.citygrid.smartlighting.entity.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByType(ContactType type);
}
