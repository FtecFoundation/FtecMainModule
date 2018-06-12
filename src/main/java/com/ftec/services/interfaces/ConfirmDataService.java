package com.ftec.services.interfaces;

import com.ftec.entities.ConfirmData;
import com.ftec.exceptions.InvalidHashException;
import com.ftec.exceptions.InvalidUserDataException;
import com.ftec.exceptions.WeakPasswordException;
import com.ftec.resources.enums.ConfirmScope;

import java.util.Optional;

public interface ConfirmDataService {
     void sendRestorePassUrl(String identifier) throws InvalidUserDataException;

     void processChangingPass(String hash, String new_pass) throws InvalidHashException, WeakPasswordException;

     Optional<ConfirmData> findById(long id);

     Optional<ConfirmData> findByUserIdAndScope(long id, ConfirmScope scope);
}
