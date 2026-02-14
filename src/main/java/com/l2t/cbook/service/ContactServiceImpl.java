package com.l2t.cbook.service;

import com.l2t.cbook.dao.ContactDao;
import com.l2t.cbook.domain.Contact;
import com.l2t.cbook.dto.ContactDto;
import com.l2t.cbook.security.AppUser;
import com.l2t.cbook.security.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactDao contactDao;
    private final EmailService emailService;
    private final AppUserRepository appUserRepository;

    @Override
    public ContactDto createContact(ContactDto contactDto) {
        log.info("Creating contact with name: {}", contactDto.getName());
        validateContact(contactDto);
        Contact contact = toEntity(contactDto);
        contact.setUser(getCurrentUser());
        Contact saved = contactDao.save(contact);
        log.info("Contact created with id: {}", saved.getId());
        return toDto(saved);
    }

    @Override
    public ContactDto getContact(UUID id) {
        log.info("Fetching contact with id: {}", id);
        AppUser appUser = getCurrentUser();
        Contact contact = contactDao.findByIdAndUser(id, appUser)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + id));
        return toDto(contact);
    }

    @Override
    public List<ContactDto> getAllContacts() {
        log.info("Fetching all contacts");
        AppUser appUser = getCurrentUser();
        List<Contact> contacts = contactDao.findAllByUser(appUser);
        log.info("Found {} contacts", contacts.size());
        return contacts.stream().map(this::toDto).toList();
    }

    @Override
    public ContactDto updateContact(UUID id, ContactDto contactDto) {
        log.info("Updating contact with id: {}", id);
        validateContact(contactDto);
        AppUser appUser = getCurrentUser();
        contactDao.findByIdAndUser(id, appUser)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + id));
        Contact contact = toEntity(contactDto);
        contact.setUser(appUser);
        Contact updated = contactDao.update(id, contact);
        log.info("Contact updated with id: {}", id);
        return toDto(updated);
    }

    @Override
    public void deleteContact(UUID id) {
        log.info("Deleting contact with id: {}", id);
        AppUser appUser = getCurrentUser();
        contactDao.findByIdAndUser(id, appUser)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + id));
        contactDao.deleteById(id);
        log.info("Contact deleted with id: {}", id);
    }

    @Override
    public List<ContactDto> searchContacts(String keyword) {
        log.info("Searching contacts with keyword: {}", keyword);
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword must not be empty");
        }
        AppUser appUser = getCurrentUser();
            List<Contact> contacts = contactDao.findByKeywordAndUser(keyword, appUser);
        log.info("Found {} contacts matching keyword: {}", contacts.size(), keyword);
        return contacts.stream().map(this::toDto).toList();
    }

    @Override
    public List<ContactDto> importContacts(MultipartFile file) {
        log.info("Importing contacts from file: {}", file.getOriginalFilename());
        // TODO: implement CSV/Excel import
        return List.of();
    }

    @Override
    public File exportContacts(String contentType) {
        log.info("Exporting contacts as: {}", contentType);
        // TODO: implement CSV/Excel export
        return null;
    }

    @Override
    public ContactDto updateContactEmail(UUID id, String email) {
        log.info("Updating email for contact with id: {}", id);
        AppUser appUser = getCurrentUser();
        contactDao.findByIdAndUser(id, appUser)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + id));
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        Contact updated = contactDao.updateEmailOnly(id, email);
        log.info("Email updated for contact with id: {}", id);
        return toDto(updated);
    }

    @Async
    @Override
    @Scheduled(cron = "* * 9 * * *")
    public void sendEmail() {
        log.info("Scheduled birthday email job started");
        List<Contact> contacts = contactDao.findAll();
        if (contacts.isEmpty()) {
            log.warn("No contacts found");
            return;
        }
        LocalDate today = LocalDate.now();
        List<Contact> birthdayContacts = contacts.stream()
                .filter(c -> c.getDob() != null
                        && c.getDob().getMonth() == today.getMonth()
                        && c.getDob().getDayOfMonth() == today.getDayOfMonth())
                .toList();
        if (birthdayContacts.isEmpty()) {
            log.info("No birthdays today");
            return;
        }
        for (Contact contact : birthdayContacts) {
            String htmlBody = buildBirthdayEmail(contact.getName());
            try {
                emailService.sendHtml(contact.getEmail(), "Happy Birthday, " + contact.getName() + "!", htmlBody);
                log.info("Birthday email sent to: {}", contact.getEmail());
            } catch (Exception e) {
                log.error("Failed to send birthday email to: {}", contact.getEmail(), e);
            }
        }
        log.info("Birthday emails sent to {} contacts", birthdayContacts.size());
    }

    private String buildBirthdayEmail(String name) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff8e1;">
                    <div style="text-align: center; background: linear-gradient(135deg, #f093fb 0%%, #f5576c 100%%); padding: 40px 20px; border-radius: 12px 12px 0 0;">
                        <img src="https://img.icons8.com/clouds/200/birthday-cake.png" alt="Birthday Cake" style="width: 120px; height: 120px;" />
                        <h1 style="color: #ffffff; margin: 10px 0 0 0; font-size: 28px;">Happy Birthday!</h1>
                    </div>
                    <div style="background-color: #ffffff; padding: 30px; border: 1px solid #e0e0e0; border-top: none; border-radius: 0 0 12px 12px;">
                        <h2 style="color: #333333; margin-top: 0;">Dear %s,</h2>
                        <p style="color: #555555; font-size: 16px; line-height: 1.6;">
                            Wishing you a wonderful birthday filled with joy, laughter, and all the things that make you happy!
                        </p>
                        <p style="color: #555555; font-size: 16px; line-height: 1.6;">
                            May this special day bring you everything your heart desires. Have an amazing celebration!
                        </p>
                        <div style="text-align: center; margin: 30px 0;">
                            <span style="background: linear-gradient(135deg, #f093fb 0%%, #f5576c 100%%); color: #ffffff; padding: 14px 32px; border-radius: 8px; font-size: 16px; font-weight: bold; display: inline-block;">
                                Have a Great Day!
                            </span>
                        </div>
                        <hr style="border: none; border-top: 1px solid #eeeeee; margin: 20px 0;" />
                        <p style="color: #999999; font-size: 12px; text-align: center;">
                            Contact Book Application | Built with love
                        </p>
                    </div>
                </div>
                """.formatted(name);
    }

    private void validateContact(ContactDto contactDto) {
        if (contactDto.getName() == null || contactDto.getName().isBlank()) {
            throw new IllegalArgumentException("Contact name must not be empty");
        }
        if (contactDto.getEmail() == null || contactDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Contact email must not be empty");
        }
        if (contactDto.getMobile() == null || contactDto.getMobile().isBlank()) {
            throw new IllegalArgumentException("Contact mobile must not be empty");
        }
    }

    private Contact toEntity(ContactDto dto) {
        Contact contact = new Contact();
        contact.setId(dto.getId());
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setMobile(dto.getMobile());
        contact.setDob(dto.getDob());
        return contact;
    }

    private ContactDto toDto(Contact contact) {
        ContactDto dto = new ContactDto();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setMobile(contact.getMobile());
        dto.setDob(contact.getDob());
        return dto;
    }

    private AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void callGreetingsApi(){
        String url = "https://cbook-qc7i.onrender.com/api/v1/cbook/greetings";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("Greetings API response: {}", response);
    }
}
