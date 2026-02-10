package com.l2t.cbook.mapper;

import com.l2t.cbook.domain.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class ContactRowMapper implements RowMapper<Contact> {
    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact contact = new Contact();
        contact.setId(UUID.fromString(rs.getString("id")));
        contact.setName(rs.getString("name"));
        contact.setEmail(rs.getString("email"));
        contact.setDob(rs.getDate("dob").toLocalDate());
        contact.setMobile(rs.getString("mobile"));
        return contact;
    }
    

    
}
