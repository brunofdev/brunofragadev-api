package com.brunofragadev.usuarios;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapeador {
    public Usuario mapearNovoUsuario(CadastrarUsuarioDTO dto) {
        Usuario cliente = new Usuario();
        cliente.setNome(dto.nome());
        cliente.setSenha(dto.senha());
        cliente.setEmail(dto.email());
        cliente.setUserName(dto.userName());
        cliente.setRole(Role.USER);
        cliente.setContaAtiva(true);
        return cliente;
    }
    public UsuarioDTO mapearUsuarioParaUsuarioDTO(Usuario novoCliente) {
        return new UsuarioDTO(
                novoCliente.getId(),
                novoCliente.getNome(),
                novoCliente.getUsername(),
                novoCliente.getEmail(),
                novoCliente.getRole(),
                novoCliente.isContaAtiva()
        );
    }
    public List<UsuarioDTO> mapearListaUsuarioParaUsuarioDTO(List<Usuario> usuarios){
        return usuarios.stream()
                .map(usuario -> mapearUsuarioParaUsuarioDTO(usuario)).toList();
    }
}
