package com.brunofragadev.usuarios;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
    @Column(name="email", nullable = false, unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "conta_ativa", nullable = false)
    private boolean contaAtiva;
    @Column(name = "codigo_verificacao", nullable = false)
    private String codigoVerificacao;
    @Column(name = "expiracao_codigo")
    private LocalDateTime expiracaoCodigo;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "profissao")
    private String profissao;
    @Column(name = "pais")
    private String pais;
    @Column(name = "cidade")
    String cidade;
    @Column(name = "fotoperfil")
    String fotoperfil;
    @Column(name = "github")
    String github;
    @Column(name = "linkedin")
    String linkedin;
    @Column(name = "bio")
    String bio;



    public Role getRole(){
        return (this.role == null) ? Role.USER : this.role;
    }

    // --- MÉTODOS OBRIGATÓRIOS DO SPRING SECURITY (UserDetails) ---

    // CORREÇÃO 2: Este é o método que faltava!
    // Ele traduz o seu Enum "UserRole" para o que o Spring entende ("GrantedAuthority")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Se a role for nula, assume o mínimo possível (USER)
        if (this.role == null) return List.of(new SimpleGrantedAuthority("ROLE_USER"));

        switch (this.role) {
            case ADMIN3:
                // ADMIN3 manda em tudo: É Admin 3, 2, 1 e User ao mesmo tempo.
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN3"),
                        new SimpleGrantedAuthority("ROLE_ADMIN2"),
                        new SimpleGrantedAuthority("ROLE_ADMIN1"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );

            case ADMIN2:
                // ADMIN2 manda no nível dele para baixo: É Admin 2, 1 e User.
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN2"),
                        new SimpleGrantedAuthority("ROLE_ADMIN1"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );

            case ADMIN1:
                // ADMIN1 é Admin 1 e User.
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN1"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );
            default: // USER
                // Usuário comum só tem permissão básica.
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    // O Spring Security chama getPassword(), mas sua variável chama "senha".
    // Precisamos apontar um para o outro.
    @Override
    public String getPassword() {
        return senha;
    }

    // O Spring chama getUsername(), mas sua variável chama "userName".
    @Override
    public String getUsername() {
        return userName;
    }

    // Configurações de validade da conta (Retornar true = conta sempre ativa)
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
        return true;
    }
}

