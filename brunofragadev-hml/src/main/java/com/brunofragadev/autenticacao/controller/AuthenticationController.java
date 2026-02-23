package com.brunofragadev.autenticacao.controller;



import com.brunofragadev.autenticacao.dto.UsuarioLoginResponseDTO;
import com.brunofragadev.autenticacao.dto.CredenciaisDTO;
import com.brunofragadev.autenticacao.service.AuthenticationService;
import com.brunofragadev.utils.retorno_padrao_api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioLoginResponseDTO>> loginCliente(@Valid @RequestBody CredenciaisDTO credentialsDTO) {
        UsuarioLoginResponseDTO loginResult = authenticationService.loginCliente(credentialsDTO);
        return ResponseEntity.ok(ApiResponse.success("Usuário autenticado com sucesso", loginResult));
    }
    @GetMapping("/validar-admin")
    @PreAuthorize("hasRole('ADMIN3')")
    public ResponseEntity<String> validarAcessoAdmin() {
        return ResponseEntity.ok("Acesso Autorizado");
    }

}
