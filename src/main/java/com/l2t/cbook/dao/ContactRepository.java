package com.l2t.cbook.dao;

import com.l2t.cbook.domain.Contact;
import com.l2t.cbook.security.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

    List<Contact> findByNameLikeOrMobileLikeOrEmailLike(String name,String mobile,String email);
    Optional<Contact> findByIdAndUser(UUID id, AppUser user);
    List<Contact> findAllByUser(AppUser user);

    @Query("SELECT c FROM Contact c WHERE c.user = :user AND (c.name LIKE :keyword OR c.mobile LIKE :keyword OR c.email LIKE :keyword)")
    List<Contact> findByUserAndKeyword(@Param("user") AppUser user, @Param("keyword") String keyword);
}
