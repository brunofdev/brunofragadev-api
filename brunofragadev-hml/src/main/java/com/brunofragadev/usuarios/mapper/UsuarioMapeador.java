package com.brunofragadev.usuarios.mapper;

import com.brunofragadev.usuarios.dto.entrada.AtualizarDadosPerfilDTO;
import com.brunofragadev.usuarios.entity.Role;
import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.usuarios.dto.entrada.CadastrarUsuarioDTO;
import com.brunofragadev.usuarios.dto.saida.UsuarioDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UsuarioMapeador {
    public Usuario mapearNovoUsuario(CadastrarUsuarioDTO dto, String senhaCriptografada) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setSenha(senhaCriptografada);
        usuario.setEmail(dto.email().toUpperCase());
        usuario.setUserName(dto.userName().toUpperCase());
        usuario.setNomePublico(dto.nomePublico());
        return usuario;
    }

    public Usuario mapearNovoUsuarioSocialGoogle(String emailFormatado, String nome, String fotoUrl, String senhaCriptografada, String userNameFinal){
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(emailFormatado);
        novoUsuario.setNome(nome);
        novoUsuario.setUserName(userNameFinal);
        novoUsuario.setFotoperfil(fotoUrl);
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setContaAtiva(true);
        novoUsuario.setRole(Role.USER);
        return novoUsuario;
    }
    public UsuarioDTO mapearUsuarioParaUsuarioDTO(@NonNull Usuario novoUsuario) {
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
