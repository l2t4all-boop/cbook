package com.l2t.cbook.dao;

import com.l2t.cbook.domain.ApiKeyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKeyDetails, UUID> {

    Optional<ApiKeyDetails> findByApiKey(String apiKey);

    Optional<ApiKeyDetails> findByEmail(String email);
}