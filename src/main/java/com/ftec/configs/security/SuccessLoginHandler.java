package com.ftec.configs.security;

import com.ftec.repositories.interfaces.UserDAO;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SuccessLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserDAO userDAO;

    private RequestCache requestCache = new HttpSessionRequestCache();

    public SuccessLoginHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
        request.getSession().setAttribute("balance", userDAO.getAuthenticatedUser().getBalance());
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            getRedirectStrategy().sendRedirect(request, response, "/cabinet");
            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request
                .getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

        public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
