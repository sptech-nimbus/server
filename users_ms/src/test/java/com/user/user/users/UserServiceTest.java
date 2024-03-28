package com.user.user.users;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.user.user.domain.user.User;
import com.user.user.repository.UserRepository;
import com.user.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService service;

    @Mock
    UserRepository repo;

    User user;
}
