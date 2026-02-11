package com.l2t.cbook.controller;

import com.l2t.cbook.dto.ApiResponse;
import com.l2t.cbook.dto.ContactDto;
import com.l2t.cbook.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cbook")
@Tag(name = "Contact", description = "Contact management APIs")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Create a new contact")
    public ApiResponse<ContactDto> createContact(@RequestBody ContactDto contactDto) {
        ContactDto created = contactService.createContact(contactDto);
        return ApiResponse.success("Contact created successfully", created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a contact by ID")
    public ApiResponse<ContactDto> getContact(@Parameter(description = "Contact ID") @PathVariable UUID id) {
        ContactDto contact = contactService.getContact(id);
        return ApiResponse.success("Contact fetched successfully", contact);
    }

    @GetMapping
    @Operation(summary = "Get all contacts")
    public ApiResponse<List<ContactDto>> getAllContacts() {
        List<ContactDto> contacts = contactService.getAllContacts();
        return ApiResponse.success("Contacts fetched successfully", contacts);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a contact")
    public ApiResponse<ContactDto> updateContact(@Parameter(description = "Contact ID") @PathVariable UUID id, @RequestBody ContactDto contactDto) {
        ContactDto updated = contactService.updateContact(id, contactDto);
        return ApiResponse.success("Contact updated successfully", updated);
    }

    @PatchMapping("/{id}/email")
    @Operation(summary = "Update contact email only")
    public ApiResponse<ContactDto> updateContactEmail(@Parameter(description = "Contact ID") @PathVariable UUID id,
                                                      @Parameter(description = "New email") @RequestParam String email) {
        ContactDto updated = contactService.updateContactEmail(id, email);
        return ApiResponse.success("Contact email updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contact")
    public ApiResponse<Void> deleteContact(@Parameter(description = "Contact ID") @PathVariable UUID id) {
        contactService.deleteContact(id);
        return ApiResponse.success("Contact deleted successfully");
    }

    @GetMapping("/search")
    @Operation(summary = "Search contacts by keyword")
    public ApiResponse<List<ContactDto>> searchContacts(@Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<ContactDto> contacts = contactService.searchContacts(keyword);
        return ApiResponse.success("Contacts searched successfully", contacts);
    }

    @PostMapping("/import")
    @Operation(summary = "Import contacts from file")
    public ApiResponse<List<ContactDto>> importContacts(@Parameter(description = "CSV/Excel file") @RequestParam("file") MultipartFile file) {
        List<ContactDto> contacts = contactService.importContacts(file);
        return ApiResponse.success("Contacts imported successfully", contacts);
    }

    @GetMapping("/export")
    @Operation(summary = "Export contacts")
    public ApiResponse<Void> exportContacts(@Parameter(description = "Content type (csv/excel)") @RequestParam String contentType) {
        contactService.exportContacts(contentType);
        return ApiResponse.success("Contacts exported successfully");
    }

    @PostMapping("/send-email")
    @Operation(summary = "Send email")
    public ApiResponse<Void> sendEmail() {
        contactService.sendEmail();
        return ApiResponse.success("Sending email triggered successfully");
    }

    @GetMapping("/greetings")
    @Operation(summary = "Sends greetings message")
    public ApiResponse<String> greetings(){
        return ApiResponse.success("Greetings from CBOOK application");
    }
}