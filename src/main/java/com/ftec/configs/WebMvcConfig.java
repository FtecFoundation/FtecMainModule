package com.ftec.configs;

import com.ftec.configs.middlewares.CORSMiddleware;
import com.ftec.configs.middlewares.TokenSecurityMiddleware;
import com.ftec.configs.middlewares.TutorialMiddleware;
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

    @Autowired
    public WebMvcConfig(TokenService tokenService, Resources resources) {
        this.tokenService = tokenService;
        this.resources = resources;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenSecurityMiddleware(tokenService)).addPathPatterns("/cabinet","/cabinet/**","/changeUserSetting","/logout");
//        registry.addInterceptor(new BanMiddleware()).addPathPatterns("/","/*","/**").excludePathPatterns("/API/**","/API/*","/error/banned");
        registry.addInterceptor(new TutorialMiddleware()).addPathPatterns("/cabinet","/cabinet/**").excludePathPatterns("/cabinet/tutorial","/cabinet/tutorial/*");
        registry.addInterceptor(new CORSMiddleware()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticPath = resources.getUploadPath();
        if (staticPath != null) {
            registry.addResourceHandler("/images/**").addResourceLocations("file:///" + staticPath);
        }
    }
}