package springBoot.core.c_config.a_security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springBoot.core.c_config.b_authentication.CustomAuthProvider;
import springBoot.core.c_config.b_authentication.CustomJwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final String[] white_list_urlsPaths = {"/public/auth/api/**"
            ,"/v2/api-docs"
            ,"/v2/api-docs/**"
            ,"/v3/api-docs/"
            ,"/v3/api-docs/**"
            ,"/swagger-resources"
            ,"/swagger-resources/**"
            ,"/configuration-ui"
            ,"/configuration-ui/**"
            ,"/configuration/security"
            ,"/configuration/security/**"
            ,"/swagger-ui"
            ,"/swagger-ui/**"
            ,"/swagger-ui.html"
            ,"/webjars"
            ,"/webjars/**"
    };

    @Autowired
    private CustomJwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private CustomAuthProvider customAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(request -> request.requestMatchers(white_list_urlsPaths)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(customAuthProvider.authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
