package com.l2t.cbook.dao;

import com.l2t.cbook.domain.Contact;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactDao {
    Contact save(Contact contact);
    Optional<Contact> findById(UUID id);
    List<Contact> findAll();
    Contact update(UUID id, Contact contact);
    void deleteById(UUID id);
    List<Contact> findByKeyword(String keyword);
    Contact updateEmailOnly(UUID id, String email);
}
