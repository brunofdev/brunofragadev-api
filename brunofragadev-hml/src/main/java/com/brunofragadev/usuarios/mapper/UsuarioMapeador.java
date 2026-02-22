package com.brunofragadev.usuarios.mapper;

import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.usuarios.dto.CadastrarUsuarioDTO;
import com.brunofragadev.usuarios.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapeador {
    public Usuario mapearNovoUsuario(CadastrarUsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setSenha(dto.senha());
        usuario.setEmail(dto.email());
        usuario.setUserName(dto.userName());
        return usuario;
    }
    public UsuarioDTO mapearUsuarioParaUsuarioDTO(Usuario novoUsuario) {
        return new UsuarioDTO(
                novoUsuario.getId(),
                novoUsuario.getNome(),
                novoUsuario.getUsername(),
                novoUsuario.getEmail(),
                novoUsuario.getRole(),
                novoUsuario.isContaAtiva(),
                novoUsuario.getCidade(),
                novoUsuario.getGithub(),
                novoUsuario.getProfissao(),
                novoUsuario.getBio(),
                novoUsuario.getFotoperfil(),
                novoUsuario.getLinkedin(),
                novoUsuario.getPais(),
                novoUsuario.getTelefone()
        );
    }
    public List<UsuarioDTO> mapearListaUsuarioParaUsuarioDTO(List<Usuario> usuarios){
        return usuarios.stream()
                .map(usuario -> mapearUsuarioParaUsuarioDTO(usuario)).toList();
    }
}
