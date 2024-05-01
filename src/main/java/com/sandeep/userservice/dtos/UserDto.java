package com.sandeep.userservice.dtos;

import com.sandeep.userservice.models.Role;
import com.sandeep.userservice.models.User;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    @ManyToMany
    private List<Role> roles;
    private boolean isEmailVerified;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setRoles(user.getRoles());
        userDto.setEmailVerified(user.isEmailVerified());
        return userDto;
    }
}
