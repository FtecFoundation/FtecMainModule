package com.ftec.controllers;

import com.ftec.repositories.RestoreDataDAO;
import com.ftec.services.interfaces.RestoreDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestoreController {

    @Autowired
    RestoreDataService restoreDataService;

    @Autowired
    RestoreDataDAO restoreDataDAO;

    @PostMapping("/sendRestoreUrl")
    public ResponseEntity<String> getRestoreUrlToEmail(@RequestParam(name = "username",required = false) String username,
                                             @RequestParam(name = "email"   ,required = false) String email) {

        if(username == null && email == null) return new ResponseEntity<>("Login and Email is empty!",HttpStatus.BAD_REQUEST);

        try {
            if (username != null) restoreDataService.sendRestorePassUrlByUsername(username);
            if (email != null) restoreDataService.sendRestorePassUrlByEmail(email);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changePass")
    public ResponseEntity<String> changePass(@RequestParam(name = "hash") String hash, @RequestParam("new_pass") String new_pass) {
        try {
            if(!restoreDataService.isHashValid(hash)) return new ResponseEntity<>("Invalid hash!", HttpStatus.BAD_REQUEST);

            restoreDataService.changePass(restoreDataDAO.findIdByHash(hash), hash, new_pass);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
