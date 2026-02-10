package com.l2t.cbook.service;

import com.l2t.cbook.dto.ContactDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface ContactService {


    ContactDto createContact(ContactDto contactDto);

    ContactDto getContact(UUID id);

    List<ContactDto> getAllContacts();

    ContactDto updateContact(UUID id, ContactDto contactDto);

    void deleteContact(UUID id);

    List<ContactDto> searchContacts(String keyword);

    List<ContactDto> importContacts(MultipartFile file);

    File exportContacts(String contentType);

    void sendEmail();
}
