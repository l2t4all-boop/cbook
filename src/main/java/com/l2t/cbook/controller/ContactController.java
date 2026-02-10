package com.l2t.cbook.controller;

import com.l2t.cbook.dto.ContactDto;
import com.l2t.cbook.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cbook")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/save")
    public ResponseEntity<ContactDto> createContact(@RequestBody ContactDto contactDto) {
        return new ResponseEntity<>(contactService.createContact(contactDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContact(@PathVariable UUID id) {
        return ResponseEntity.ok(contactService.getContact(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactDto>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ContactDto> updateContact(@PathVariable UUID id, @RequestBody ContactDto contactDto) {
        return ResponseEntity.ok(contactService.updateContact(id, contactDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable UUID id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactDto>> searchContacts(@RequestParam String keyword) {
        return ResponseEntity.ok(contactService.searchContacts(keyword));
    }
}