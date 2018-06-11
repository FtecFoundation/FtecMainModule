package com.ftec.configs;

import com.ftec.configs.middlewares.CORSMiddleware;
import com.ftec.configs.middlewares.SupportMiddleware;
import com.ftec.configs.middlewares.TokenSecurityMiddleware;
import com.ftec.configs.middlewares.TutorialMiddleware;
import com.ftec.controllers.TicketController;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenService tokenService;
    private final Resources resources;
    private final UserDAO userDAO;

    @Autowired
    public WebMvcConfig(TokenService tokenService, Resources resources, UserDAO userDAO) {
        this.tokenService = tokenService;
        this.resources = resources;
        this.userDAO = userDAO;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenSecurityMiddleware(tokenService)).addPathPatterns("/cabinet", "/cabinet/**", "/changeUserSetting", "/logout")
                .order(0);
        registry.addInterceptor(new SupportMiddleware(userDAO)).addPathPatterns(TicketController.ADM_PREF + "/**")
                .order(1);

//        registry.addInterceptor(new BanMiddleware()).addPathPatterns("/","/*","/**").excludePathPatterns("/API/**","/API/*","/error/banned");
        registry.addInterceptor(new TutorialMiddleware()).addPathPatterns("/cabinet", "/cabinet/**").excludePathPatterns("/cabinet/tutorial", "/cabinet/tutorial/*");
        registry.addInterceptor(new CORSMiddleware()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticPath = resources.getUploadPath();
        if (staticPath != null) {
            registry.addResourceHandler("/images/**").addResourceLocations("file:///" + staticPath);
            registry.addResourceHandler("/static/htmlstatic/**").addResourceLocations("file:///" + staticPath);
        }
    }
}