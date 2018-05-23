package com.ftec.configs;

import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Locale;

@Service
public class CustomLocaleResolver extends CookieLocaleResolver {
    private final UserDAO userDAO;

    @Autowired
    public CustomLocaleResolver(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        if(!isAvailable(locale.getLanguage())) return;
        super.setLocale(request, response, locale);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext.getAuthentication()!=null && securityContext.getAuthentication().isAuthenticated()) {
            String userName = securityContext.getAuthentication().getName();
            userDAO.updateUserLanguage(locale.getLanguage(), userName);
        }
    }
    private boolean isAvailable(String newLang){
        for(String lang: Resources.availableLanguages){
            if(lang.equals(newLang)) return true;
        }
        return false;
    }
}
