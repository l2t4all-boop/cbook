package com.l2t.cbook.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Contact {
    private UUID id;
    private String name;
    private String email;
    private String mobile;
    private LocalDate dob;
}
