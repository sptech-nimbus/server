package com.user.user.services;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.user.user.models.user.UserDTO;

@Service
public class UserService {
    public Boolean checkAllUserCredencials(UserDTO newUser) {
        return checkUserEmail(newUser.email())
                && checkUserPassword(newUser.password());
    }

    public Boolean checkUserEmail(String email) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(email)
                .matches();
    }

    public Boolean checkUserPassword(String password) {
        return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
                .matcher(password).matches();
    }
}