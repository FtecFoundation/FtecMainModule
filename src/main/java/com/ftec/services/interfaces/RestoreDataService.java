package com.ftec.services.interfaces;

import com.ftec.entities.RestoreData;
import com.ftec.exceptions.InvalidHashException;
import com.ftec.exceptions.InvalidUserDataException;
import com.ftec.exceptions.WeakPasswordException;

import java.util.Optional;

public interface RestoreDataService {
     void sendRestorePassUrl(String identifier) throws InvalidUserDataException;

     void processChangingPass(String hash, String new_pass) throws InvalidHashException, WeakPasswordException;

     Optional<RestoreData> findById(long id);
}
