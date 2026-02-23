package com.brunofragadev.autenticacao.service;



import com.brunofragadev.autenticacao.dto.CredenciaisDTO;
import com.brunofragadev.autenticacao.dto.UsuarioLoginResponseDTO;
import com.brunofragadev.configs.JwtProvider;
import com.brunofragadev.usuarios.dto.UsuarioDTO;
import com.brunofragadev.usuarios.service.UsuarioServico;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtProvider jwtProvider;
    private final UsuarioServico usuarioServico;


    public AuthenticationService( JwtProvider jwtProvider, UsuarioServico usuarioServico){
        this.jwtProvider = jwtProvider;
        this.usuarioServico = usuarioServico;
    }

    public UsuarioLoginResponseDTO loginCliente(CredenciaisDTO credentials) {
        UsuarioDTO usuario = usuarioServico.autenticarUsuario(credentials.userName(), credentials.senha());
        String token = jwtProvider.generateToken(usuario.userName(),usuario.role());
        return new UsuarioLoginResponseDTO(token, usuario);
    }

}
