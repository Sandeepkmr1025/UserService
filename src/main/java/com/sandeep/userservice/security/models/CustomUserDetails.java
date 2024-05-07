package com.sandeep.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sandeep.userservice.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonDeserialize
public class CustomUserDetails implements UserDetails {

    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<CustomGrantedAuthority> authorities;
    private Long userId;

    public CustomUserDetails() {}

    public CustomUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getHashedPassword();
        this.accountNonExpired = true;
        this.accountNonLocked =  true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.authorities = new ArrayList<CustomGrantedAuthority>();

        // user.getRoles().forEach(role -> authorities.add(new CustomGrantedAuthority(role)));
//        List<Role> roles = user.getRoles();
//        for (Role role : roles) {
//            authorities.add(new CustomGrantedAuthority(role));
//        }
        user.getRoles().stream().map(CustomGrantedAuthority::new).forEach(authorities::add);
        this.userId = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getUserId() {
        return userId;
    }

}
