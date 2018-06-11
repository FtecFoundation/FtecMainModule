package com.ftec.configs.middlewares;

import com.ftec.repositories.UserDAO;
import com.ftec.resources.enums.UserRole;
import com.ftec.services.interfaces.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SupportMiddleware implements HandlerInterceptor {

    private  final UserDAO userDAO;

    public SupportMiddleware(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            UserRole userRole = userDAO.findById(TokenService.getUserIdFromToken(request.getHeader(TokenService.TOKEN_NAME))).get().getUserRole();
            if(!userRole.equals(UserRole.SUPPORT)){
                response.setStatus(403);
                return false;
            }
        } catch (Exception e){
            response.setStatus(400);
            response.addHeader("error", "Unexpected error");
            return false;
        }
        return true;
    }
}
