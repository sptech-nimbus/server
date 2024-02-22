package com.user.user.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.user.user.models.user.User;
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
}
