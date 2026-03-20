package com.brunofragadev.usuarios.entity;

import com.brunofragadev.usuarios.exceptions.VerificationCodeInvalidException;
import com.brunofragadev.utils.utilitarios.CodigoVerificacao;
import jakarta.persistence.*;
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
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", nullable = false)
    private String nome;
    @Column(name="user_name", nullable = false, unique = true)
    private String userName;
    @Column(name="senha", nullable = false)
    private String senha;
    @Column(name="nome_publico", nullable = true)
    private String nomePublico ;
    @Column(name="email", nullable = false, unique = true)
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
    @Column(name = "usuario_anonimo" , nullable = true)
    private Boolean isAnonimo;
    @Embedded
    private CodigoVerificacao codigoVerificacao;

    public static Usuario criar(String nome, String userName,
                                String email, String senhaCriptografada,
                                String nomePublico) {
        Usuario u = new Usuario();
        u.nome = nome;
        u.userName = userName.toUpperCase();
        u.email = email.toUpperCase();
        u.senha = senhaCriptografada;
        u.nomePublico = nomePublico;
        u.role = Role.USER;
        u.contaAtiva = false;
        return u;
    }
    public static Usuario criarViaGoogle(String nome, String userName,
                                         String email, String senhaCriptografada,
                                         String fotoUrl) {
        Usuario u = new Usuario();
        u.nome = nome;
        u.userName = userName.toUpperCase(); // regra centralizada
        u.email = email.toUpperCase();       // regra centralizada
        u.senha = senhaCriptografada;
        u.fotoperfil = fotoUrl;
        u.role = Role.USER;
        u.contaAtiva = true;                 // Google já valida o email
        return u;
    }

    public void ativarConta(){
        this.contaAtiva = true;
        this.codigoVerificacao = null;
    }
    public void definirCodigoVerificacao(String codigo, LocalDateTime expiracao) {
        this.codigoVerificacao = new CodigoVerificacao(codigo, expiracao);
    }
    public void alterarSenha(String senhaCriptografada) {
        this.senha = senhaCriptografada;
        this.codigoVerificacao = null;
    }
    public void validarCodigo(String tentativa) {
        if (this.codigoVerificacao == null)
            throw new VerificationCodeInvalidException("Nenhum código ativo");
        if (this.codigoVerificacao.estaExpirado())
            throw new VerificationCodeInvalidException("Código expirado");
        if (!this.codigoVerificacao.corresponde(tentativa))
            throw new VerificationCodeInvalidException("Código inválido");
    }
    public void definirRole(Role role) {
        this.role = (role == null) ? Role.USER : role;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNomePublico(String nomePublico) {
        this.nomePublico = nomePublico;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAnonimo(Boolean anonimo) {
        isAnonimo = anonimo;
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

    public Role getRole(){
        return (this.role == null) ? Role.USER : this.role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null) return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        switch (this.role) {
            case ADMIN3:
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN3"),
                        new SimpleGrantedAuthority("ROLE_ADMIN2"),
                        new SimpleGrantedAuthority("ROLE_ADMIN1"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );
            case ADMIN2:
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN2"),
                        new SimpleGrantedAuthority("ROLE_ADMIN1"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );

            case ADMIN1:
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN1"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );
            default:
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
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

