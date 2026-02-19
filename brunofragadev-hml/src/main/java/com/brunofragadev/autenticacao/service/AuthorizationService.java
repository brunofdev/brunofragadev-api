package com.brunofragadev.autenticacao.service;

import com.brunofragadev.usuarios.UsuarioRepositorio;

import com.brunofragadev.usuarios.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UserNotFoundException {
        var usuario = usuarioRepositorio.findByUserName(userName);
        if(usuario.isPresent()){
            return usuario.get();
        }
        throw new UserNotFoundException("Nenhum usuario enviado no token foi localizado");
    }
}
