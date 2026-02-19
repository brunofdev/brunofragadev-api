package com.brunofragadev.usuarios;

import com.brunofragadev.usuarios.exceptions.InvalidCredentialsException;
import com.brunofragadev.usuarios.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServico {
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioValidador usuarioValidador;
    private final UsuarioMapeador usuarioMapeador;
    private final PasswordEncoder codificadorSenha;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio,
                          UsuarioValidador usuarioValidador,
                          UsuarioMapeador usuarioMapeador,
                          PasswordEncoder codificadorSenha) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioValidador = usuarioValidador;
        this.usuarioMapeador = usuarioMapeador;
        this.codificadorSenha = codificadorSenha;
    }

    public UsuarioDTO cadastrarUsuario(CadastrarUsuarioDTO dtoComSenhaCodificada) {
        usuarioValidador.validarNovoUsuario(dtoComSenhaCodificada, false);
        Usuario usuario = usuarioMapeador.mapearNovoUsuario(dtoComSenhaCodificada);
        usuario.setRole(Role.USER);
        usuarioRepositorio.save(usuario);
        return usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
    }


    public boolean existePorNomeUsuario(String nomeUsuario) {
        return usuarioRepositorio.existsByUserName(nomeUsuario);
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioMapeador.mapearListaUsuarioParaUsuarioDTO(usuarioRepositorio.findAll());
    }

    public UsuarioDTO autenticarUsuario(String userName, String senha) {
        Usuario usuario = usuarioRepositorio.findByUserName(userName).orElseThrow(() -> new UserNotFoundException("nome de Usuario não encontrado"));
        if (!codificadorSenha.matches(senha, usuario.getSenha())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }
        return usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
    }
}

