package com.example.jobservice.config;

import com.example.jobservice.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v1/job/category/create",
                                "/v1/job/category/update",
                                "/v1/job/category/deleteCategoryById/" +

                                "/v1/job/job/create",
                                "/v1/job/job/update",
                                "/v1/job/job/deleteJobById/",
                                "/v1/job/offer/deleteOfferById/**",
                                "/v1/job/offer/update",

                                "/v1/job/advert/update",
                                "/v1/job/advert/delete/").hasRole("ADMIN")

                        .requestMatchers(
                                "/v1/job/category/getAll",
                                "/v1/job/category/getCategoryById/",

                                "/v1/job/job/getAll",
                                "/v1/job/job/getJobById/{id}",
                                "/v1/job/job/getJobsThatFitYourNeeds/{needs}",

                                "/v1/job/offer/getOfferById/{offerId}",
                                "/v1/job/offer/getOffersByAdvertId/{advertId}",
                                "/v1/job/offer/getOffersByUserId/{userId}",
                                "/v1/job/offer/makeAnOffer",

                                "/v1/job/advert/getAll",
                                "/v1/job/advert/getAdvertByUserId/{userId}",
                                "/v1/job/advert/create",
                                "/v1/job/advert/getAdvertById/{id}").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
