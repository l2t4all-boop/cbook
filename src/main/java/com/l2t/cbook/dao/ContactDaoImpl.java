package com.l2t.cbook.dao;

import com.l2t.cbook.domain.Contact;
import com.l2t.cbook.mapper.ContactRowMapper;
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

    @Override
    public Contact save(Contact contact) {
        if (contact.getId() == null) {
            contact.setId(UUID.randomUUID());
        }
        String sql = "INSERT INTO contact (id, name, email, mobile, dob) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            contact.getId(), 
            contact.getName(), 
            contact.getEmail(), 
            contact.getMobile(), 
            contact.getDob());
        return contact;
    }

    @Override
    public Optional<Contact> findById(UUID id) {
        String sql = "SELECT id, name, email, mobile, dob FROM contact WHERE id = ?";
        try {
            Contact contact = jdbcTemplate.queryForObject(sql, new ContactRowMapper(), id);
            return Optional.ofNullable(contact);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Contact> findAll() {
        String sql = "SELECT id, name, email, mobile, dob FROM contact";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    @Override
    public Contact update(UUID id, Contact contact) {
        String sql = "UPDATE contact SET name = ?, email = ?, mobile = ?, dob = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
            contact.getName(), 
            contact.getEmail(), 
            contact.getMobile(), 
            contact.getDob(), 
            id);
        contact.setId(id);
        return contact;
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "DELETE FROM contact WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Contact> findByKeyword(String keyword) {
        String sql = "SELECT id, name, email, mobile, dob FROM contact WHERE name LIKE ? OR mobile LIKE ?";
        String wildcard = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new ContactRowMapper(), wildcard, wildcard);
    }
}