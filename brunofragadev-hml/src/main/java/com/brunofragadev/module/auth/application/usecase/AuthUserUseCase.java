package com.brunofragadev.module.auth.application.usecase;

import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.api.dto.request.CredentialsRequest;
import com.brunofragadev.module.auth.api.dto.response.UserLoginResponse;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.application.usecase.ValidateCredentialsUseCase;
import org.springframework.stereotype.Service;

@Service
public class AuthUserUseCase {

    private final JwtProvider jwtProvider;
    private final ValidateCredentialsUseCase validateCredentialsUseCase;

    public AuthUserUseCase(JwtProvider jwtProvider, ValidateCredentialsUseCase validateCredentialsUseCase){
        this.jwtProvider = jwtProvider;
        this.validateCredentialsUseCase = validateCredentialsUseCase;
    }

    public UserLoginResponse execute(CredentialsRequest credentials) {
        UserDTO user = validateCredentialsUseCase.execute(credentials.userName(), credentials.senha());
        String token = jwtProvider.generateToken(user.userName(), user.role());
        return new UserLoginResponse(token, user);
    }
}