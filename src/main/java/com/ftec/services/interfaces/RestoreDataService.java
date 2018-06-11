package com.ftec.services.interfaces;

import com.ftec.entities.RestoreData;
import com.ftec.exceptions.RestoreException;

import java.util.Optional;

public interface RestoreDataService {
     void sendRestorePassUrl(String identifier) throws Exception;

     void processChangingPass(String hash, String new_pass) throws RestoreException;

     Optional<RestoreData> findById(long id);
}
