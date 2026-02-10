package com.l2t.cbook.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ContactDto {
    private UUID id;
    private String name;
    private String email;
    private String mobile;
    private LocalDate dob;
}
