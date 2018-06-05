package com.ftec.configs;

import com.ftec.configs.middlewares.TokenSecurityMiddleware;
import com.ftec.configs.middlewares.TutorialMiddleware;
import com.ftec.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final TokenService tokenService;

	@Autowired
	public WebMvcConfig(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TokenSecurityMiddleware(tokenService)).addPathPatterns("/cabinet","/cabinet/**");
//        registry.addInterceptor(new BanMiddleware()).addPathPatterns("/","/*","/**").excludePathPatterns("/API/**","/API/*","/error/banned");
		registry.addInterceptor(new TutorialMiddleware()).addPathPatterns("/cabinet","/cabinet/**").excludePathPatterns("/cabinet/tutorial","/cabinet/tutorial/*");
	}
}