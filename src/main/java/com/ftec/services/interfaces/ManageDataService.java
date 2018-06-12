package com.ftec.services.interfaces;

import com.ftec.exceptions.InvalidHashException;
import com.ftec.exceptions.InvalidUserDataException;
import com.ftec.exceptions.WeakPasswordException;

public interface DataService {
     void sendRestorePassUrl(String identifier) throws InvalidUserDataException;

     void processChangingPass(String hash, String new_pass) throws InvalidHashException, WeakPasswordException;

     void sendConfirmEmail(String email, long userId);

     void confirmEmail(String hash);
}
