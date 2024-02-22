package com.user.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.user.user.models.user.User;
import com.user.user.models.user.UserDTO;
import com.user.user.models.user.User.UserRes;

@Service
public class UserService {
    public List<UserRes> getUserResList(List<User> users) {
        List<UserRes> usersRes = new ArrayList<>();

        for (User user : users) {
            usersRes.add(new UserRes(user.getFirstName(), user.getLastName(), user.getUserId()));
        }

        return usersRes;
    }

    public Boolean checkAllUserCredencials(UserDTO newUser) {
        return matchRegex(newUser.email(), "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }

    public Boolean matchRegex(String value, String regex) {
        return Pattern.compile(regex)
                .matcher(value)
                .matches();
    }
}
