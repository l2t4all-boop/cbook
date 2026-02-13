package com.l2t.cbook.controller;

import com.l2t.cbook.dto.ApiResponse;
import com.l2t.cbook.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cbook/api-keys")
@Tag(name = "API Key", description = "API Key management APIs")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    @Operation(summary = "Generate a new API key")
    public ApiResponse<String> generateApiKey(@Parameter(description = "Email address") @RequestParam String email) {
        String apiKey = apiKeyService.generateApiKey(email);
        return ApiResponse.success("API key generated successfully", apiKey);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate an API key")
    public ApiResponse<Boolean> validateApiKey(@Parameter(description = "API key to validate") @RequestParam String apiKey) {
        boolean valid = apiKeyService.isValidApiKey(apiKey);
        return ApiResponse.success(valid ? "API key is valid" : "API key is invalid", valid);
    }
}