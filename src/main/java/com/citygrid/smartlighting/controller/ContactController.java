package com.citygrid.smartlighting.controller;

import com.citygrid.smartlighting.entity.Contact;
import com.citygrid.smartlighting.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Contact> create(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.create(contact));
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAll() {
        return ResponseEntity.ok(contactService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
