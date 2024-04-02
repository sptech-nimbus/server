package com.user.user.domain.user;

import com.user.user.domain.user.authentication.dto.UserTokenDTO;

public class UserMapper {
    public static User of(UserDTO userdDto){
        User user = new User();
        user.setEmail(userdDto.email());
        user.setPassword(userdDto.password());
        return user;
    }


    public static UserTokenDTO of(User user, String token){

        UserTokenDTO userTokenDTO = new UserTokenDTO();

        userTokenDTO.setUserId(user.getId());
        userTokenDTO.setEmail(user.getEmail());
        userTokenDTO.setToken(token);

        return userTokenDTO;
    }
    
}