package com.brunofragadev.module.user.application.mapper;

import com.brunofragadev.module.user.api.dto.request.UpdateProfileData;
import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public User toNewUser(UserRegistrationRequest dto, String encodedPassword) {
        return User.criar(
                dto.nome(),
                dto.userName(),
                dto.email(),
                encodedPassword,
                dto.nomePublico()
        );
    }

    public User toNewGoogleUser(String formattedEmail,
                                String name,
                                String photoUrl,
                                String encodedPassword,
                                String finalUserName) {
        return User.criarViaGoogle(
                name,
                finalUserName,
                formattedEmail,
                encodedPassword,
                photoUrl);
    }

    public UserDTO toDTO(@NonNull User user) {
        return new UserDTO(
                user.getId(),
                user.getNome(),
                user.getUsername(),
                user.getNomePublico(),
                user.getIsAnonimo(),
                user.getEmail(),
                user.getRole(),
                user.isContaAtiva(),
                user.getCidade(),
                user.getGithub(),
                user.getProfissao(),
                user.getBio(),
                user.getFotoperfil(),
                user.getLinkedin(),
                user.getPais(),
                user.getTelefone()
        );
    }

    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .toList();
    }

    public User updateEntityFromData(User user, UpdateProfileData newData) {
        user.setProfissao(newData.profissao());
        user.setCidade(newData.cidade());
        user.setPais(newData.pais());
        user.setGithub(newData.gitHub());
        user.setLinkedin(newData.linkedin());
        user.setFotoperfil(newData.fotoPerfil());
        user.setTelefone(newData.telefone());
        user.setBio(newData.bio());
        user.setNomePublico(newData.nomePublico());
        user.setAnonimo(newData.isAnonimo());
        return user;
    }
}