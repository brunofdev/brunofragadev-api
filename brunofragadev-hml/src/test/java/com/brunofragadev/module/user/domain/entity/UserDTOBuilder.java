package com.brunofragadev.module.user.domain.entity;

import com.brunofragadev.module.user.api.dto.response.UserDTO;

public class UserDTOBuilder {

    private Long id;
    private String nome;
    private String userName;
    private String nomePublico;
    private Boolean isAnonimo;
    private String email;
    private Role role;
    private Boolean contaAtiva;
    private String cidade;
    private String gitHub;
    private String profissao;
    private String bio;
    private String fotoPerfil;
    private String linkedin;
    private String pais;
    private String telefone;

    private UserDTOBuilder() {}

    public static UserDTOBuilder umUsuario() {
        return new UserDTOBuilder();
    }

    // ─── Valores padrão prontos para uso nos testes ──────────────────────────

    public static UserDTOBuilder umUsuarioPadrao() {
        return new UserDTOBuilder()
                .comId(1L)
                .comNome("Bruno de Fraga")
                .comUserName("brunofdev")
                .comNomePublico("Bruno Dev")
                .comIsAnonimo(false)
                .comEmail("bruno@email.com")
                .comRole(Role.USER)
                .comContaAtiva(true)
                .comCidade("Canoas/RS")
                .comGitHub("https://github.com/brunofdev")
                .comProfissao("Desenvolvedor Backend")
                .comBio("Engenheiro de Software apaixonado por Clean Architecture e SOLID.")
                .comFotoPerfil("https://brunofragadev.com/images/perfil/foto.jpg")
                .comLinkedin("https://linkedin.com/in/bruno-de-fraga")
                .comPais("Brasil")
                .comTelefone("+5548999999999");
    }

    public static UserDTOBuilder umAdmin() {
        return umUsuarioPadrao()
                .comRole(Role.ADMIN3)
                .comUserName("admin");
    }

    public static UserDTOBuilder umUsuarioInativo() {
        return umUsuarioPadrao()
                .comContaAtiva(false);
    }

    // ─── Setters fluentes ────────────────────────────────────────────────────

    public UserDTOBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public UserDTOBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public UserDTOBuilder comUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserDTOBuilder comNomePublico(String nomePublico) {
        this.nomePublico = nomePublico;
        return this;
    }

    public UserDTOBuilder comIsAnonimo(Boolean isAnonimo) {
        this.isAnonimo = isAnonimo;
        return this;
    }

    public UserDTOBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public UserDTOBuilder comRole(Role role) {
        this.role = role;
        return this;
    }

    public UserDTOBuilder comContaAtiva(Boolean contaAtiva) {
        this.contaAtiva = contaAtiva;
        return this;
    }

    public UserDTOBuilder comCidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public UserDTOBuilder comGitHub(String gitHub) {
        this.gitHub = gitHub;
        return this;
    }

    public UserDTOBuilder comProfissao(String profissao) {
        this.profissao = profissao;
        return this;
    }

    public UserDTOBuilder comBio(String bio) {
        this.bio = bio;
        return this;
    }

    public UserDTOBuilder comFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
        return this;
    }

    public UserDTOBuilder comLinkedin(String linkedin) {
        this.linkedin = linkedin;
        return this;
    }

    public UserDTOBuilder comPais(String pais) {
        this.pais = pais;
        return this;
    }

    public UserDTOBuilder comTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    // ─── Build ───────────────────────────────────────────────────────────────

    public UserDTO build() {
        return new UserDTO(
                id, nome, userName, nomePublico, isAnonimo,
                email, role, contaAtiva, cidade, gitHub,
                profissao, bio, fotoPerfil, linkedin, pais, telefone
        );
    }
}
