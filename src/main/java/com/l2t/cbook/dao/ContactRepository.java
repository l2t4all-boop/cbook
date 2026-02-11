package com.l2t.cbook.dao;

import com.l2t.cbook.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

    List<Contact> findByNameLikeOrMobileLikeOrEmailLike(String name,String mobile,String email);
}
