package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.Contact;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact create(Contact contact) {
        return contactRepository.save(contact);
    }

    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    public Contact getById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found: " + id));
    }

    public void delete(Long id) {
        contactRepository.deleteById(id);
    }
}
