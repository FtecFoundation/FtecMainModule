package com.ftec.entities;

import com.ftec.resources.TelegramNotifications;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class TelegramSettings {
    @Id
    private long userId;
    private String accessCode;
    private boolean enabled;
    private String enabledNotifications;
    private long linkedUserChatId;
    private String linkedUsername;

    public TelegramSettings() {
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

    public void setLinkedUserChatId(long linkedUserId) {
        this.linkedUserChatId = linkedUserId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    public boolean isSettingEnabled(TelegramNotifications notification){
        return TelegramSettings.getNotificationsEnabledArray(enabledNotifications).contains(notification);
    }

    public String getEnabledNotifications() {
        return enabledNotifications;
    }

    public void setEnabledNotifications(String notificationsEnabled) {
        this.enabledNotifications = notificationsEnabled;
    }

    public static List<TelegramNotifications> getNotificationsEnabledArray(String settings) {
        if(settings.isEmpty()) return new ArrayList<>();
        List<TelegramNotifications> notifications = new ArrayList<>();
        for(String notification: settings.split(" ")){
            notifications.add(TelegramNotifications.values()[Integer.parseInt(notification)]);
        }
        return notifications;
    }

    public static String convertNotifications(List<TelegramNotifications> notificationsEnabled) {
        return notificationsEnabled.stream().map(notification -> notification.ordinal()+"").collect(Collectors.joining(" "));
    }
}
