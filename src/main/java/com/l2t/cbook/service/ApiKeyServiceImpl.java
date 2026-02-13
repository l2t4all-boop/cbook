package com.l2t.cbook.service;

import com.l2t.cbook.dao.ApiKeyRepository;
import com.l2t.cbook.domain.ApiKeyDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final EmailService emailService;

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int API_KEY_LENGTH = 24;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateApiKey(String email) {
        log.info("Generating API key for email: {}", email);
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        String apiKey = generateRandomKey();
        Optional<ApiKeyDetails> optApiKey = apiKeyRepository.findByEmail(email);
        ApiKeyDetails apiKeyDetails = null;
        if (optApiKey.isPresent()) {
            apiKeyDetails = optApiKey.get();
            apiKeyDetails.setApiKey(apiKey);
        } else {
            apiKeyDetails = new ApiKeyDetails();
            apiKeyDetails.setEmail(email);
            apiKeyDetails.setApiKey(apiKey);
        }
        apiKeyRepository.save(apiKeyDetails);
        emailService.sendHtml(email, "Your API Key", buildApiKeyEmail(apiKey));
        log.info("API key generated and emailed to: {}", email);
        return apiKey;
    }

    @Override
    public boolean isValidApiKey(String apiKey) {
        log.info("Validating API key");
        if (apiKey == null || apiKey.isBlank()) {
            return false;
        }
        return apiKeyRepository.findByApiKey(apiKey).isPresent();
    }

    private String buildApiKeyEmail(String apiKey) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f5f5f5;">
                    <div style="text-align: center; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 40px 20px; border-radius: 12px 12px 0 0;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 28px;">Your API Key</h1>
                    </div>
                    <div style="background-color: #ffffff; padding: 30px; border: 1px solid #e0e0e0; border-top: none; border-radius: 0 0 12px 12px;">
                        <p style="color: #555555; font-size: 16px; line-height: 1.6;">
                            Your API key has been generated successfully. Please keep it safe and do not share it with anyone.
                        </p>
                        <div style="text-align: center; margin: 30px 0;">
                            <code style="background-color: #f0f0f0; padding: 14px 32px; border-radius: 8px; font-size: 20px; font-weight: bold; letter-spacing: 2px; display: inline-block; color: #333333;">
                                %s
                            </code>
                        </div>
                        <hr style="border: none; border-top: 1px solid #eeeeee; margin: 20px 0;" />
                        <p style="color: #999999; font-size: 12px; text-align: center;">
                            Contact Book Application | Built with love
                        </p>
                    </div>
                </div>
                """.formatted(apiKey);
    }

    private String generateRandomKey() {
        StringBuilder sb = new StringBuilder(API_KEY_LENGTH);
        for (int i = 0; i < API_KEY_LENGTH; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}