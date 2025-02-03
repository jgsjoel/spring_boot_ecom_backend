package com.ddelight.ddAPI.Authentication.config;

import com.ddelight.ddAPI.Authentication.filters.AuthFilter;
import com.ddelight.ddAPI.Authentication.services.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
public class AuthConfig {

    private AuthFilter authFilter;

    public AuthConfig(AuthFilter authFilter){
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {

        http.cors(corsCustomizer-> corsCustomizer.configurationSource(corsConfigurationSource()));
        http.csrf(csrf->csrf.disable());
        http.httpBasic(basic->basic.disable());
        http.formLogin(flg->flg.disable());

        http.authorizeHttpRequests(request->
                request.requestMatchers(
                        "api/v1/auth/login",
                                "api/v1/auth/refresh",
                                "/api/v1/auth/register",
                                "/api/v1/auth/verify",
                                "/api/v1/stripe/webhook",
                                "/api/v1/orders/**",
                                "/websocket/**",
                                //admin
                                "/api/v1/admin/category/**",
                                "/api/v1/admin/products/**",
                                "/api/v1/admin/banner/**"
                        )
//                        .anonymous()
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000","https://*.stripe.com"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type","Refresh-Token"));
        corsConfig.setAllowCredentials(true);
        corsConfig.addExposedHeader("Authorization");  // Add this
        corsConfig.addExposedHeader("Refresh-Token");  // Add this

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager provideAuthManager(
            CustomUserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(dao);
    }

}
