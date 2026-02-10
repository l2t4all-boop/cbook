package com.l2t.cbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    private UUID id;
    private String name;
    private String email;
    private String mobile;
    private LocalDate dob;
}