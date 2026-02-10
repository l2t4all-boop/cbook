package com.l2t.cbook.service;

import com.l2t.cbook.dao.ContactDao;
import com.l2t.cbook.domain.Contact;
import com.l2t.cbook.dto.ContactDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactDao contactDao;

    @Override
    public ContactDto createContact(ContactDto contactDto) {
        Contact contact = mapToEntity(contactDto);
        return mapToDto(contactDao.save(contact));
    }

    @Override
    public ContactDto getContact(UUID id) {
        return contactDao.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
    }

    @Override
    public List<ContactDto> getAllContacts() {
        return contactDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ContactDto updateContact(UUID id, ContactDto contactDto) {
        Contact contact = mapToEntity(contactDto);
        return mapToDto(contactDao.update(id, contact));
    }

    @Override
    public void deleteContact(UUID id) {
        contactDao.deleteById(id);
    }

    @Override
    public List<ContactDto> searchContacts(String keyword) {
        return contactDao.findByKeyword(keyword).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactDto> importContacts(MultipartFile file) {
        log.info("Importing file: {}", file.getOriginalFilename());
        return List.of();
    }

    @Override
    public File exportContacts(String contentType) {
        log.info("Exporting as: {}", contentType);
        return null;
    }

    @Override
    public void sendEmail() {
    }


    private Contact mapToEntity(ContactDto dto) {
        Contact contact = new Contact();
        contact.setId(dto.getId());
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setMobile(dto.getMobile());
        contact.setDob(dto.getDob());
        return contact;
    }

    private ContactDto mapToDto(Contact contact) {
        ContactDto dto = new ContactDto();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setMobile(contact.getMobile());
        dto.setDob(contact.getDob());
        return dto;
    }
}