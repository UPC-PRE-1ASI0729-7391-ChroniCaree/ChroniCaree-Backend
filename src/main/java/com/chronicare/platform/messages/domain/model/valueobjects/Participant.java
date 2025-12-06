package com.chronicare.platform.messages.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class Participant {
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    private ParticipantRole role;
    
    private Instant joinedAt;
    private Boolean muted;

    public Participant() {}

    public Participant(Long userId, ParticipantRole role, Instant joinedAt) {
        this.userId = Objects.requireNonNull(userId, "User ID is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.joinedAt = Objects.requireNonNull(joinedAt, "Joined at timestamp is required");
        this.muted = false;
    }

    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ParticipantRole getRole() {
        return role;
    }
    
    public void setRole(ParticipantRole role) {
        this.role = role;
    }
    
    public void setRole(String roleStr) {
        try {
            this.role = ParticipantRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException _) {
            this.role = ParticipantRole.PATIENT;
        }
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Boolean isMuted() {
        return muted != null && muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
