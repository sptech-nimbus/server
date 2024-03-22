package com.user.user.users;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.user.user.domains.user.User;
import com.user.user.repositories.UserRepository;
import com.user.user.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService service;

    @Mock
    UserRepository repo;

    User user;
}
