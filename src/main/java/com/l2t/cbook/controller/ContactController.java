package com.l2t.cbook.controller;

import com.l2t.cbook.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cbook")
public class ContactController {

    private final ContactService contactService;


}
