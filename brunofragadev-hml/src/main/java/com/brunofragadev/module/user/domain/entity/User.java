package com.brunofragadev.module.user.domain.entity;

import com.brunofragadev.module.user.domain.exception.VerificationCodeInvalidException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String nome;

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @NotBlank
    @Column(name = "senha", nullable = false)
    private String senha;

    @Size(min = 4, max = 30)
    @Column(name = "nome_publico")
    private String nomePublico;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "conta_ativa", nullable = false)
    private boolean contaAtiva;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "pais")
    private String pais;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "fotoperfil")
    private String fotoperfil;

    @Column(name = "github")
    private String github;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "bio")
    private String bio;

    @Column(name = "usuario_anonimo")
    private Boolean isAnonimo;

    @Embedded
    private VerificationCode verificationCode;


    /**
     * Cria um novo usuário com conta inativa aguardando verificação.
     * O email e userName são normalizados para uppercase automaticamente.
     * A senha deve ser fornecida já criptografada.
     *
     * @throws IllegalArgumentException se qualquer campo obrigatório for nulo ou vazio
     */
    public static User criar(String nome, String userName,
                             String email, String senhaCriptografada,
                             String nomePublico) {
        validarCamposObrigatorios(nome, userName, email, senhaCriptografada);
        User u = new User();
        u.nome = nome;
        u.userName = userName.toUpperCase();
        u.email = email.toUpperCase();
        u.senha = senhaCriptografada;
        u.nomePublico = nomePublico;
        u.role = Role.USER;
        u.contaAtiva = false;
        return u;
    }

    /**
     * Cria um novo usuário autenticado via Google com conta já ativa.
     * O email e userName são normalizados para uppercase automaticamente.
     * A senha deve ser fornecida já criptografada — gerada aleatoriamente pelo sistema.
     *
     * @throws IllegalArgumentException se qualquer campo obrigatório for nulo ou vazio
     */
    public static User criarViaGoogle(String nome, String userName,
                                      String email, String senhaCriptografada,
                                      String fotoUrl) {
        validarCamposObrigatorios(nome, userName, email, senhaCriptografada);
        User u = new User();
        u.nome = nome;
        u.userName = userName.toUpperCase();
        u.email = email.toUpperCase();
        u.senha = senhaCriptografada;
        u.fotoperfil = fotoUrl;
        u.role = Role.USER;
        u.contaAtiva = true;
        return u;
    }

    public void ativarConta() {
        this.contaAtiva = true;
        this.verificationCode = null;
    }

    public void definirCodigoVerificacao(String codigo, LocalDateTime expiracao) {
        this.verificationCode = new VerificationCode(codigo, expiracao);
    }

    public void alterarSenha(String senhaCriptografada) {
        if (senhaCriptografada == null || senhaCriptografada.isBlank())
            throw new IllegalArgumentException("Senha não pode ser vazia");
        this.senha = senhaCriptografada;
        this.verificationCode = null;
    }

    public void validarCodigo(String tentativa) {
        if (this.verificationCode == null)
            throw new VerificationCodeInvalidException("Nenhum código ativo");
        if (this.verificationCode.estaExpirado())
            throw new VerificationCodeInvalidException("Código expirado");
        if (!this.verificationCode.corresponde(tentativa))
            throw new VerificationCodeInvalidException("Código inválido");
    }

    public void definirRole(Role role) {
        this.role = (role == null) ? Role.USER : role;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUserName(String userName) {
        this.userName = (userName != null) ? userName.toUpperCase() : null;
    }

    public void setNomePublico(String nomePublico) {
        this.nomePublico = nomePublico;
    }

    public void setEmail(String email) {
        this.email = (email != null) ? email.toUpperCase() : null;
    }

    public void setAnonimo(Boolean anonimo) {
        this.isAnonimo = anonimo;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Role getRole() {
        return (this.role == null) ? Role.USER : this.role;
    }


    private static void validarCamposObrigatorios(String nome, String userName,
                                                  String email, String senha) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio");
        if (userName == null || userName.isBlank())
            throw new IllegalArgumentException("Username não pode ser vazio");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email não pode ser vazio");
        if (senha == null || senha.isBlank())
            throw new IllegalArgumentException("Senha não pode ser vazia");
    }

    // -------------------------------------------------------------------------
    // Spring Security — UserDetails
    // -------------------------------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null) return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return switch (this.role) {
            case ADMIN3 -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN3"),
                    new SimpleGrantedAuthority("ROLE_ADMIN2"),
                    new SimpleGrantedAuthority("ROLE_ADMIN1"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case ADMIN2 -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN2"),
                    new SimpleGrantedAuthority("ROLE_ADMIN1"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case ADMIN1 -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN1"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            default -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
        };
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.contaAtiva;
    }
}