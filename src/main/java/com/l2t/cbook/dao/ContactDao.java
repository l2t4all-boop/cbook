package com.l2t.cbook.dao;

import com.l2t.cbook.domain.Contact;

import com.l2t.cbook.security.AppUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactDao {
    Contact save(Contact contact);
    Optional<Contact> findById(UUID id);
    Optional<Contact> findByIdAndUser(UUID id, AppUser user);
    List<Contact> findAll();
    List<Contact> findAllByUser(AppUser user);
    Contact update(UUID id, Contact contact);
    void deleteById(UUID id);
    List<Contact> findByKeyword(String keyword);
    List<Contact> findByKeywordAndUser(String keyword, AppUser user);
    Contact updateEmailOnly(UUID id, String email);
}
