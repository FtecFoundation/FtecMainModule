package com.ftec.services.interfaces;

import com.ftec.exceptions.RestoreException;

public interface RestoreDataService {
     void sendRestorePassUrl(String identifier) throws Exception;

     void checkAndChange(String hash, String new_pass) throws RestoreException;
}
