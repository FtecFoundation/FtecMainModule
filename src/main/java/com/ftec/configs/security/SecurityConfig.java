package com.ftec.configs.security;

import com.ftec.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final WebAuthenticationDetailsSource authenticationDetails;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(WebAuthenticationDetailsSource authenticationDetails, UserDetailsService userDetailsService) {
        this.authenticationDetails = authenticationDetails;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(getDAOAuthProvider());
        auth.authenticationProvider(new GoogleAuthProvider());
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

    private AuthenticationProvider getDAOAuthProvider() {
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
