package com.ftec.configs;

import com.ftec.configs.middlewares.BanMiddleware;
import com.ftec.configs.middlewares.TutorialMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
    @Autowired
    private LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BanMiddleware()).addPathPatterns("/","/*","/**").excludePathPatterns("/API/**","/API/*","/error/banned");
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(new TutorialMiddleware()).addPathPatterns("/cabinet","/cabinet/**").excludePathPatterns("/cabinet/tutorial","/cabinet/tutorial/*");
    }

    @Bean
    public LocaleChangeInterceptor localeInterceptor(){
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean("messageSource")
    @Profile({"development","staging"})
    public MessageSource messageSourceDev(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.addBasenames("classpath:locales/Messages");
        messageSource.addBasenames("classpath:locales/ValidationMessages");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }
    @Bean("messageSource")
    @Profile("production")
    public MessageSource messageSourceProd(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.addBasenames("classpath:locales/Messages");
        messageSource.addBasenames("classpath:locales/ValidationMessages");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver(){
        return new CookieLocaleResolver();
    }
}
