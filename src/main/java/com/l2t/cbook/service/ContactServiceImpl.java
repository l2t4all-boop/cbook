package com.l2t.cbook.service;

import com.l2t.cbook.dao.ContactDao;
import com.l2t.cbook.dto.ContactDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactServiceImpl implements  ContactService{

    private final ContactDao contactDao;

    @Override
    public ContactDto createContact(ContactDto contactDto) {
        return null;
    }

    @Override
    public ContactDto getContact(UUID id) {
        return null;
    }

    @Override
    public List<ContactDto> getAllContacts() {
        return List.of();
    }

    @Override
    public ContactDto updateContact(UUID id, ContactDto contactDto) {
        return null;
    }

    @Override
    public void deleteContact(UUID id) {

    }

    @Override
    public List<ContactDto> searchContacts(String keyword) {
        return List.of();
    }

    @Override
    public List<ContactDto> importContacts(MultipartFile file) {
        return List.of();
    }

    @Override
    public File exportContacts(String contentType) {
        return null;
    }

    @Override
    public void sendEmail() {

    }
}
