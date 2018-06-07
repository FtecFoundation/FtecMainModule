package com.ftec.services.interfaces;

import com.ftec.exceptions.EmailNotExistException;
import com.ftec.exceptions.UserNotExistsException;

import java.io.IOException;

public interface RestoreDataService {
     void sendRestorePassUrlByUsername(String username) throws UserNotExistsException;
     void sendRestorePassUrlByEmail(String email) throws EmailNotExistException;

     void changePass(long userId, String restore_url, String new_pass) throws IOException;

     boolean isHashValid(String hash);
}
