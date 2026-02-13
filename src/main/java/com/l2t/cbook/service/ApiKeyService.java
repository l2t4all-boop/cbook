package com.l2t.cbook.service;

public interface ApiKeyService {

    String generateApiKey(String email);

    boolean isValidApiKey(String apiKey);
}