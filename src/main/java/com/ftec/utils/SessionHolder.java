package com.ftec.utils;

import com.ftec.configs.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class SessionHolder {
    public static HttpSession getSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    public static long getCurrentUserId(){
        return ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static void setSessionBalance(double amount){
        getSession().setAttribute("balance", amount);
    }

    public static void addToSessionBalance(double amount){
        Object balance = getSession().getAttribute("balance");
        if(balance==null) getSession().setAttribute("balance", amount);
        else getSession().setAttribute("balance", ((double)balance)+amount);
    }
}
