package com.user.user.config.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.user.user.config.security.jwt.GerenciadorTokenJwt;
import com.user.user.service.AuthenticationService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguracao {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AutenticacaoEntryPoint autenticacaoJwtEntryPoint;

    private static final AntPathRequestMatcher[] URLS_PERMITIDAS = {
            new AntPathRequestMatcher("/swagger"),
            new AntPathRequestMatcher("/error/**"),
            new AntPathRequestMatcher("/actuator/*"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/swagger.yaml"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/swagger-ui/index.html"),
            new AntPathRequestMatcher("/configuration/ui"),
            new AntPathRequestMatcher("/swagger-resources"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/configuration/security"),
            new AntPathRequestMatcher("/users/login"),
            new AntPathRequestMatcher("/users/login", "OPTIONS"),
            new AntPathRequestMatcher("/users", "POST"),
            new AntPathRequestMatcher("/users", "OPTIONS"),
            new AntPathRequestMatcher("/api/public/**"),
            new AntPathRequestMatcher("/api/public/**", "OPTIONS"),
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/h2-console/**", "OPTIONS"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/v3/api-docs/**", "OPTIONS"),
            new AntPathRequestMatcher("/codes/validate-code"),
            new AntPathRequestMatcher("/codes/validate-code", "OPTIONS"),
            new AntPathRequestMatcher("/teams/ms-get-team/**"),
            new AntPathRequestMatcher("/teams/ms-get-team/**", "OPTIONS"),
            new AntPathRequestMatcher("/api/public/authenticate"),
            new AntPathRequestMatcher("/api/public/authenticate", "OPTIONS"),
            new AntPathRequestMatcher("/coaches/ms-get-coach/**"),
            new AntPathRequestMatcher("/coaches/ms-get-coach/**", "OPTIONS"),
            new AntPathRequestMatcher("/teams/ms-change-level/**"),
            new AntPathRequestMatcher("/users/change-password/**"),
            new AntPathRequestMatcher("/users/change-password/**", "OPTIONS"),
            new AntPathRequestMatcher("/users/ms-get-chat-user/**"),
            new AntPathRequestMatcher("/users/ms-get-chat-user/**", "OPTIONS"),
            new AntPathRequestMatcher("/athletes/ms-get-athlete/**"),
            new AntPathRequestMatcher("/athletes/ms-get-athlete/**", "OPTIONS"),
            new AntPathRequestMatcher("/users/change-password-request"),
            new AntPathRequestMatcher("/users/change-password-request", "OPTIONS"),
            new AntPathRequestMatcher("/athlete-historics/ms-by-games/**"),
            new AntPathRequestMatcher("/athlete-historics/ms-by-games/**", "OPTIONS")
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URLS_PERMITIDAS)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(autenticacaoJwtEntryPoint))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilterBean(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .authenticationProvider(new AutenticacaoProvider(authenticationService, passwordEncoder()));
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AutenticacaoEntryPoint jwtAuthenticationEntryPointBean() {
        return new AutenticacaoEntryPoint();
    }

    @Bean
    public AuthenticationFilter jwtAuthenticationFilterBean() {
        return new AuthenticationFilter(authenticationService, jwtAuthenticationUtilBean());
    }

    @Bean
    public GerenciadorTokenJwt jwtAuthenticationUtilBean() {
        return new GerenciadorTokenJwt();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        String publicIp = System.getenv("PUBLIC_IP");
        String publicIp2 = System.getenv("PUBLIC_IP2");
        String corsOriginDNS = System.getenv("CORS_ORIGIN");
        configuracao.setAllowedOrigins(Arrays.asList("http://events-ms", "http://gateway", "http://localhost:5173", "http://" + publicIp2, "http://" + corsOriginDNS, "http://" + publicIp));
        configuracao.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.TRACE.name()
        ));
        configuracao.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        configuracao.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION));
        configuracao.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
        origem.registerCorsConfiguration("/**", configuracao);
        return origem;
    }
}