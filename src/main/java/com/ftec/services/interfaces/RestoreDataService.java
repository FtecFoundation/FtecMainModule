package com.ftec.services.interfaces;

import com.ftec.entities.RestoreData;
import com.ftec.exceptions.RestoreException;
import com.ftec.exceptions.WeakPasswordException;

import java.util.Optional;

public interface RestoreDataService {
     void sendRestorePassUrl(String identifier) throws Exception;

     void processChangingPass(String hash, String new_pass) throws RestoreException, WeakPasswordException;

     Optional<RestoreData> findById(long id);
}
