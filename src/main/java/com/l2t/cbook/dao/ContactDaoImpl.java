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
        contact.setId(UUID.randomUUID());
        String sql = "insert into contact (id, name, email, mobile, dob) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contact.getId(), contact.getName(), contact.getEmail(), contact.getMobile(), contact.getDob());
        return contact;
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
        String sql = "update contact set name = ?, email = ?, mobile = ?, dob = ? where id = ?";
        jdbcTemplate.update(sql, contact.getName(), contact.getEmail(), contact.getMobile(), contact.getDob(), id);
        contact.setId(id);
        return contact;
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "delete from contact where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Contact updateEmailOnly(UUID id, String email) {
        String sql = "update contact set email = ? where id = ?";
        jdbcTemplate.update(sql, email, id);
        return findById(id).orElse(null);
    }

    @Override
    public List<Contact> findByKeyword(String keyword) {
        String sql = "select id, name, email, mobile, dob from contact where name like ? or email like ? or mobile like ?";
        String pattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new ContactRowMapper(), pattern, pattern, pattern);
    }
}
