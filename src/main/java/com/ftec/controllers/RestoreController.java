package com.ftec.controllers;

import com.ftec.exceptions.InvalidHashException;
import com.ftec.exceptions.InvalidUserDataException;
import com.ftec.exceptions.WeakPasswordException;
import com.ftec.resources.enums.Statuses;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.RestoreDataService;
import com.ftec.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class RestoreController {

    private final
    RestoreDataService restoreDataService;


    @Autowired
    public RestoreController(RestoreDataService restoreDataService) {
        this.restoreDataService = restoreDataService;
    }

    @PostMapping(value = "/sendRestoreUrl", consumes = "application/json")
    public MvcResponse getRestoreUrlToEmail(@RequestParam(name = "data") String data, HttpServletResponse response) {
        try {
            restoreDataService.sendRestorePassUrl(data);
        }
        catch (InvalidUserDataException e){
            response.setStatus(400);
            return MvcResponse.getMvcErrorResponse(Statuses.InvalidUserData.getStatus(), e.getMessage());
        }
        catch (Exception e) {
            Logger.logException("While executing sending restore url", e, true);
            response.setStatus(500);
            return MvcResponse.getMvcErrorResponse(Statuses.UnexpectedError.getStatus(), "Unexpected error");
        }

        return new MvcResponse(200);
    }


    @PostMapping(value = "/changePass", consumes = "application/json")
    public MvcResponse changePass(@RequestParam(name = "hash") String hash, @RequestParam("new_pass") String new_pass, HttpServletResponse response) {
        try {
            restoreDataService.processChangingPass(hash, new_pass);
        }
        catch (InvalidHashException e){
            response.setStatus(400);
            return MvcResponse.getMvcErrorResponse(Statuses.InvalidHash.getStatus(),"Invalid hash!");
        } catch (WeakPasswordException e){
            response.setStatus(400);
            return MvcResponse.getMvcErrorResponse(Statuses.WeakPassword.getStatus(),"Weak password!");
        }
        catch (Exception e){
            Logger.logException("While executing changing pass", e, true);
            response.setStatus(500);
            return MvcResponse.getMvcErrorResponse(Statuses.UnexpectedError.getStatus(),"Unexpected error");
        }

        return new MvcResponse(Statuses.Ok.getStatus());
    }
}
