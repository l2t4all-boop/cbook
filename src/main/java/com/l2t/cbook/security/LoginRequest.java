package com.l2t.cbook.security;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
