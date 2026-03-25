package com.brunofragadev.module.auth.api.dto;


import com.brunofragadev.module.user.api.dto.response.UserDTO;

public record UserLoginResponse(String token, UserDTO clienteDTO) {
}
