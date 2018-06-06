package com.ftec.services.interfaces;

import com.ftec.controllers.ChangeSettingController;
import com.ftec.exceptions.UserNotExistsException;

public interface ChangeSettingsService {
    void updatePreferences(ChangeSettingController.UserUpdate userUpdate, long userId) throws UserNotExistsException;
}
