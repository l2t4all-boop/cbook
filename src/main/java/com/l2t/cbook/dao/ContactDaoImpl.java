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
        return null;
    }

    @Override
    public Optional<Contact> findById(UUID id) {
        String sql = "select id, name, email, mobile, dob from contact where id = ?";
        try {
            Contact contact = jdbcTemplate.queryForObject(sql, new ContactRowMapper(), id);
            return Optional.of(contact);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Contact> findAll() {
        String sql = "select id, name, email, mobile, dob from contact";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    @Override
    public Contact update(UUID id, Contact contact) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public List<Contact> findByKeyword(String keyword) {
        return List.of();
    }
}
