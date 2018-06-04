package com.ftec.entities;

import javax.persistence.*;

@Entity
@Table
public class TelegramSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long userId;
    private String accessCode, linkedUsername;
    private long linkedUserChatId;
    private boolean enabled;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getLinkedUsername() {
        return linkedUsername;
    }

    public void setLinkedUsername(String linkedUsername) {
        this.linkedUsername = linkedUsername;
    }

    public long getLinkedUserChatId() {
        return linkedUserChatId;
    }

    public void setLinkedUserChatId(long linkedUserChatId) {
        this.linkedUserChatId = linkedUserChatId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
