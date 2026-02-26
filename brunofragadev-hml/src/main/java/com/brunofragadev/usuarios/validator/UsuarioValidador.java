package com.brunofragadev.usuarios.validator;

import com.brunofragadev.usuarios.dto.entrada.CadastrarUsuarioDTO;
import com.brunofragadev.usuarios.exceptions.EmailAlreadyExistsException;
import com.brunofragadev.usuarios.exceptions.UsernameAlreadyExistsException;
import com.brunofragadev.usuarios.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;


    public void validarNovoUsuario(CadastrarUsuarioDTO dto, Boolean isUpdate) {
        checaEmailExiste(dto.email());
        checaUserNameExiste(dto.userName());
    }
    private void checaEmailExiste(String email){
        if(usuarioRepositorio.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email ja cadastrado no sistema");
        }
    }

    private void checaUserNameExiste(String userName){
        if(usuarioRepositorio.existsByUserName(userName)){
            throw new UsernameAlreadyExistsException(
                    "Já existe um userName com este Nome cadastrado no sistema");
        }
    }


}
