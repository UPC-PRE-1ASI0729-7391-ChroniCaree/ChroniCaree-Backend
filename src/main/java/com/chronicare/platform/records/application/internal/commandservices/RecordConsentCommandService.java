package com.chronicare.platform.records.application.internal.commandservices;

import com.chronicare.platform.records.domain.model.entities.RecordConsent;
import com.chronicare.platform.records.infrastructure.persistence.jpa.repositories.RecordConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecordConsentCommandService {

    private final RecordConsentRepository consentRepository;

    @Transactional
    public RecordConsent grantConsent(GrantConsentCommand command) {
        RecordConsent consent = RecordConsent.builder()
                .recordId(command.recordId())
                .patientId(command.patientId())
                .grantedToUserId(command.grantedToUserId())
                .grantedAt(LocalDateTime.now())
                .expiresAt(command.expiresAt())
                .purpose(command.purpose())
                .revoked(false)
                .build();

        return consentRepository.save(consent);
    }

    @Transactional
    public void revokeConsent(Long consentId) {
        RecordConsent consent = consentRepository.findById(consentId)
                .orElseThrow(() -> new IllegalArgumentException("Consent not found: " + consentId));

        consent.revoke();
        consentRepository.save(consent);
    }

    @Transactional
    public void revokeAllConsentsForRecord(Long recordId) {
        var consents = consentRepository.findByRecordIdAndRevokedFalse(recordId);
        consents.forEach(RecordConsent::revoke);
        consentRepository.saveAll(consents);
    }

    public record GrantConsentCommand(
            Long recordId,
            Long patientId,
            Long grantedToUserId,
            LocalDateTime expiresAt,
            String purpose
    ) {}
}
