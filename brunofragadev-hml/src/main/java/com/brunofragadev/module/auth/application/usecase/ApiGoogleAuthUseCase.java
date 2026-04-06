package com.brunofragadev.module.auth.application.usecase;

import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.api.dto.request.GoogleAuthRequest;
import com.brunofragadev.module.auth.api.dto.response.UserLoginResponse;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.application.usecase.ProcessGoogleLoginUseCase;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ApiGoogleAuthUseCase { // Corrigido de UserCase para UseCase

    @Value("${google.client.id}")
    private String googleClientId;

    private final ProcessGoogleLoginUseCase processGoogleLoginUseCase;
    private final JwtProvider jwtProvider;

    public ApiGoogleAuthUseCase(ProcessGoogleLoginUseCase processGoogleLoginUseCase, JwtProvider jwtProvider){
        this.processGoogleLoginUseCase = processGoogleLoginUseCase;
        this.jwtProvider = jwtProvider;
    }

    public UserLoginResponse execute(GoogleAuthRequest googleAuthRequest) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            String rawToken = googleAuthRequest.googleToken();
            String cleanToken = rawToken.replace("\"", "");

            GoogleIdToken idToken = verifier.verify(cleanToken);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String photoUrl = (String) payload.get("picture");

                UserDTO userEntity = processGoogleLoginUseCase.execute(email, name, photoUrl);

                String generatedToken = jwtProvider.generateToken(userEntity.userName(), userEntity.role());

                return new UserLoginResponse(generatedToken, userEntity);

            } else {
                throw new RuntimeException("Access Denied: Google token is tampered or expired.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with Google servers: " + e.getMessage());
        }
    }
}