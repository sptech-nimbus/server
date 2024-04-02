package com.user.user.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.user.user.service.AuthenticatioService;

public class AutenticacaoProvider implements AuthenticationProvider {

    private final AuthenticatioService userAuthenticatioService;
    private final PasswordEncoder passwordEncoder;


    public AutenticacaoProvider(AuthenticatioService userAuthenticatioService, PasswordEncoder passwordEncoder) {
        this.userAuthenticatioService = userAuthenticatioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        
        UserDetails userDetails = this.userAuthenticatioService.loadUserByUsername(username);

        if (this.passwordEncoder.matches(password, userDetails.getPassword())) {
            return new  UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }else{
            throw new BadCredentialsException("Usuário ou Senha inválidos");
        }

    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
