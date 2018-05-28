package com.ftec.configs;

import com.ftec.configs.middlewares.BanMiddleware;
import com.ftec.configs.middlewares.TutorialMiddleware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BanMiddleware()).addPathPatterns("/","/*","/**").excludePathPatterns("/API/**","/API/*","/error/banned");
        registry.addInterceptor(new TutorialMiddleware()).addPathPatterns("/cabinet","/cabinet/**").excludePathPatterns("/cabinet/tutorial","/cabinet/tutorial/*");
    }
}
