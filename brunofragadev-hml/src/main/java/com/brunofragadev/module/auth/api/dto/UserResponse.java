package com.brunofragadev.module.auth.api.dto;


import com.brunofragadev.module.user.domain.entity.Role;

public record UserResponse(String username, Role role) {
}
