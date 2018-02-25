package com.ftec.configs.security;

import com.ftec.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final SecurityService securityService;
    private final WebAuthenticationDetailsSource authenticationDetails;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(WebAuthenticationDetailsSource authenticationDetails, SecurityService securityService, UserDetailsService userDetailsService) {
        this.authenticationDetails = authenticationDetails;
        this.securityService = securityService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                    .antMatchers("/cabinet/**")
                        .hasRole("USER")
                    .antMatchers("/admin/**")
                        .hasRole("ADMIN")
                .and()
                    .formLogin()
                    .authenticationDetailsSource(authenticationDetails)
                    .loginPage("/login")
                    .usernameParameter("login")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/cabinet")
                    .failureForwardUrl("/login")
                .and()
                    .csrf()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/");
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        MysqlAuthProvider mysqlProvider = new MysqlAuthProvider(passwordEncoder());
        mysqlProvider.setPasswordEncoder(passwordEncoder());
        mysqlProvider.setUserDetailsService(userDetailsService);
        return mysqlProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
}
