package com.user.user.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.user.user.models.user.User;
import com.user.user.models.user.UserDTO;
import com.user.user.models.user.User.UserRes;

@Service
public class UserService {
    public final List<String> userTypes = Arrays.asList("Treinador", "Jogador");

    public List<UserRes> getUserResList(List<User> users) {
        List<UserRes> usersRes = new ArrayList<>();

        for (User user : users) {
            usersRes.add(new UserRes(user.getFirstName(), user.getLastName(), user.getUserType(), user.getUserId()));
        }

        return usersRes;
    }

    public UserRes convertUserToRes(User user) {
        return new UserRes(user.getFirstName(), user.getLastName(), user.getUserType(), user.getUserId());
    }

    public Boolean checkAllUserCredencials(UserDTO newUser) {
        return checkUserEmail(newUser.email())
                && checkUserPassword(newUser.password())
                && checkUserTypeExists(newUser.userType());
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

    public Boolean checkUserTypeExists(String userType) {
        return userTypes.contains(userType);
    }
}