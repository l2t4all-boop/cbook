package com.l2t.cbook.dao;

import com.l2t.cbook.domain.Contact;
import com.l2t.cbook.security.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ContactDaoImpl implements ContactDao {

    private final JdbcTemplate jdbcTemplate;
    private final ContactRepository contactRepository;

    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Optional<Contact> findById(UUID id) {
        return contactRepository.findById(id);
    }

    @Override
    public Optional<Contact> findByIdAndUser(UUID id, AppUser user) {
        return contactRepository.findByIdAndUser(id, user);
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> findAllByUser(AppUser user) {
        return contactRepository.findAllByUser(user);
    }

    @Override
    public Contact update(UUID id, Contact contact) {
        if (contactRepository.findById(id).isPresent()) {
            return contactRepository.save(contact);
        }
        throw new IllegalArgumentException("Contact not found");
    }

    @Override
    public void deleteById(UUID id) {
        contactRepository.deleteById(id);
    }

    @Override
    public Contact updateEmailOnly(UUID id, String email) {
        if (contactRepository.existsById(id)) {
            Contact contact = contactRepository.findById(id).get();
            contact.setEmail(email);
            return contactRepository.save(contact);
        }
        throw new IllegalArgumentException("Contact is not found with id " + id);
    }

    @Override
    public List<Contact> findByKeyword(String text) {
        String pattern = "%" + text + "%";
        return contactRepository.findByNameLikeOrMobileLikeOrEmailLike(pattern, pattern, pattern);
    }

    @Override
    public List<Contact> findByKeywordAndUser(String text, AppUser user) {
        String pattern = "%" + text + "%";
        return contactRepository.findByUserAndKeyword(user, pattern);
    }
}
