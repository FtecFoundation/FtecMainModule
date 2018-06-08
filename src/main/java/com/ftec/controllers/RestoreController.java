package com.ftec.controllers;

import com.ftec.exceptions.RestoreException;
import com.ftec.exceptions.UserNotExistsException;
import com.ftec.repositories.RestoreDataDAO;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.RestoreDataService;
import com.ftec.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class RestoreController {

    final
    RestoreDataService restoreDataService;

    final
    RestoreDataDAO restoreDataDAO;

    @Autowired
    public RestoreController(RestoreDataService restoreDataService, RestoreDataDAO restoreDataDAO) {
        this.restoreDataService = restoreDataService;
        this.restoreDataDAO = restoreDataDAO;
    }

    @PostMapping("/sendRestoreUrl")
    public MvcResponse getRestoreUrlToEmail(@RequestParam(name = "data") String data, HttpServletResponse response) {
        try {
            restoreDataService.sendRestorePassUrl(data);
        }
        catch (UserNotExistsException e){
            set400Status(response);
            return new MvcResponse(400, e.getMessage());
        }
        catch (Exception e) {
            Logger.logException("while serving send restore url", e, true);
            set400Status(response);
            return new MvcResponse(400,"Unexpected error");
        }
        return new MvcResponse(200);
    }

    public void set400Status(HttpServletResponse response) {
        response.setStatus(400);
    }

    @PostMapping("/changePass")
    public MvcResponse changePass(@RequestParam(name = "hash") String hash, @RequestParam("new_pass") String new_pass, HttpServletResponse response) {
        try {
            restoreDataService.checkAndChange(hash, new_pass);
        }
        catch (RestoreException e){
            set400Status(response);
            return new MvcResponse(400, e.getMessage());
        }
        catch (Exception e){
            Logger.logException("while serving send restore url", e, true);
            set400Status(response);
            return new MvcResponse(400,"Unexpected error");
        }

        return new MvcResponse(200);
    }
}
