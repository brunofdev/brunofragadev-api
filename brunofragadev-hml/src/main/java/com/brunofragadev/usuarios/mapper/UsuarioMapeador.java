package com.brunofragadev.usuarios.mapper;

import com.brunofragadev.usuarios.dto.entrada.AtualizarDadosPerfilDTO;
import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.usuarios.dto.entrada.CadastrarUsuarioDTO;
import com.brunofragadev.usuarios.dto.saida.UsuarioDTO;
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
        usuario.setNomePublico(dto.nomePublico());
        return usuario;
    }
    public UsuarioDTO mapearUsuarioParaUsuarioDTO(Usuario novoUsuario) {
        return new UsuarioDTO(
                novoUsuario.getId(),
                novoUsuario.getNome(),
                novoUsuario.getUsername(),
                novoUsuario.getNomePublico(),
                novoUsuario.getIsAnonimo(),
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
    public Usuario mapearDadosAtualizadosPerfil(Usuario usuario, AtualizarDadosPerfilDTO novosDados){
        usuario.setProfissao(novosDados.profissao());
        usuario.setCidade(novosDados.cidade());
        usuario.setPais(novosDados.pais());
        usuario.setGithub(novosDados.gitHub());
        usuario.setLinkedin(novosDados.linkedin());
        usuario.setFotoperfil(novosDados.fotoPerfil());
        usuario.setTelefone(novosDados.telefone());
        usuario.setBio(novosDados.bio());
        usuario.setNomePublico(novosDados.nomePublico());
        usuario.setIsAnonimo(novosDados.isAnonimo());
        return usuario;
    };
}
