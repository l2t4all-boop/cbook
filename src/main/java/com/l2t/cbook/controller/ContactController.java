package com.l2t.cbook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.l2t.cbook.service.ContactService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cbook")
public class ContactController {

	@Autowired
    private final ContactService contactService;

}
