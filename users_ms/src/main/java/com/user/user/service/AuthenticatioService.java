package com.user.user.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.user.user.domain.user.User;
import com.user.user.domain.user.authentication.dto.UserDetailsDTO;
import com.user.user.repository.UserRepository;

@Service
public class AuthenticatioService implements UserDetailsService {
    private final UserRepository userRepository;

    public AuthenticatioService(UserService userService, UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(username);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Usuário: %s não encontrado", username));
        }

        return new UserDetailsDTO(userOpt.get());
    }

}
