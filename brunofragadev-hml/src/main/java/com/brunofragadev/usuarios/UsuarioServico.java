package com.brunofragadev.usuarios;

import com.brunofragadev.email.ServicoDeEmail;
import com.brunofragadev.usuarios.exceptions.*;
import com.brunofragadev.utils.retorno_padrao_api.FormatadoresUteis;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UsuarioServico {
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioValidador usuarioValidador;
    private final UsuarioMapeador usuarioMapeador;
    private final PasswordEncoder codificadorSenha;
    private final ServicoDeEmail servicoDeEmail;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio,
                          UsuarioValidador usuarioValidador,
                          UsuarioMapeador usuarioMapeador,
                          PasswordEncoder codificadorSenha,
                          ServicoDeEmail servicoDeEmail) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioValidador = usuarioValidador;
        this.usuarioMapeador = usuarioMapeador;
        this.codificadorSenha = codificadorSenha;
        this.servicoDeEmail = servicoDeEmail;
    }
    @Transactional
    public UsuarioDTO cadastrarUsuario(CadastrarUsuarioDTO dtoComSenhaCodificada) {
        usuarioValidador.validarNovoUsuario(dtoComSenhaCodificada, false);
        Usuario usuario = usuarioMapeador.mapearNovoUsuario(dtoComSenhaCodificada);
        usuario.setRole(Role.USER);
        usuario.setContaAtiva(false);
        String codigoGerado = String.format("%06d", new Random().nextInt(999999));
        usuario.setCodigoVerificacao(codigoGerado);
        usuario.setExpiracaoCodigo(LocalDateTime.now().plusMinutes(5));
        usuarioRepositorio.save(usuario);
        UsuarioDTO novoUsuario = usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
        servicoDeEmail.enviarEmailDeVerificacao(novoUsuario.email(), novoUsuario.userName(), usuario.getCodigoVerificacao());
        return novoUsuario;
    }
    private void validarCodigo (Usuario usuarioParaValidar, String codigo) {
        if (!usuarioParaValidar.getCodigoVerificacao().equals(codigo)) {
            throw new VerificationCodeInvalidException("Codigo invalido");
        }
        if (usuarioParaValidar.getExpiracaoCodigo().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeInvalidException("O código de verificação expirou.");
        }
    }
    @Transactional
    public void gerarNovoCodigo (String userName){
        Usuario usuario = buscarUsuarioPorUserName(userName.toUpperCase());
        String codigoGerado = String.format("%06d", new Random().nextInt(999999));
        usuario.setCodigoVerificacao(codigoGerado);
        usuario.setExpiracaoCodigo(LocalDateTime.now().plusMinutes(5));
        usuarioRepositorio.save(usuario);
        servicoDeEmail.enviarEmailDeVerificacao(usuario.getEmail(), usuario.getUsername(), usuario.getCodigoVerificacao());
    }

    @Transactional
    public UsuarioDTO autenticarContaAtiva(AutenticarUsuarioDTO dto){
        Usuario usuario = buscarUsuarioPorUserName(dto.userName());
        validarCodigo(usuario, dto.codigo());
        usuario.setContaAtiva(true);
        usuarioRepositorio.save(usuario);
        UsuarioDTO usuarioDTO = usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
        servicoDeEmail.enviarEmailDeBoasVindas(usuarioDTO);
        return usuarioDTO;
    }
    public boolean existePorNomeUsuario(String nomeUsuario) {
        return usuarioRepositorio.existsByUserName(nomeUsuario);
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioMapeador.mapearListaUsuarioParaUsuarioDTO(usuarioRepositorio.findAll());
    }
    public Boolean verificaContaAtiva(Usuario usuario){
        return usuario.isContaAtiva();
    }
    private Usuario buscarUsuarioPorUserName(String userName){
        return usuarioRepositorio.findByUserName(userName.toUpperCase()).orElseThrow(() -> new UserNotFoundException("nome de Usuario não encontrado"));
    }
    public UsuarioDTO autenticarUsuario(String userName, String senha) {
        Usuario usuario = buscarUsuarioPorUserName(userName);
        if (!codificadorSenha.matches(senha, usuario.getSenha())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }
        return usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
    }
    private Usuario buscarPorEmail (String email){
        return usuarioRepositorio.findByEmail(email.toUpperCase()).orElseThrow(() -> new UserDontHaveEmailRegistered("Email não localizado"));
    }
    private Usuario buscarPorUserNameOuEmail (String userNameOrEmail){
        return usuarioRepositorio.findByUserNameOrEmail(userNameOrEmail).orElseThrow(() -> new UserDontHaveEmailRegistered("Email ou UserName não encontrado no sistema, por favor verifique as informações"));
    }
    @Transactional
    public UsuarioRecuperacaoSenhaDTO enviarCodigoRecuperacaoSenhaPorEmail (String userNameOuEmail){
        Usuario usuario = buscarPorUserNameOuEmail(userNameOuEmail);
        String codigoGerado = String.format("%06d", new Random().nextInt(999999));
        usuario.setCodigoVerificacao(codigoGerado);
        usuario.setExpiracaoCodigo(LocalDateTime.now().plusMinutes(5));
        usuarioRepositorio.save(usuario);
        servicoDeEmail.enviarEmailDeRecuperacaoDeSenha(usuario.getEmail(), usuario.getUsername(), codigoGerado);
        return new UsuarioRecuperacaoSenhaDTO(FormatadoresUteis.ofuscarEmail(usuario.getEmail()));
    }
    @Transactional
    public void validarCodigoRecuperacaoSenha (AutenticarUsuarioDTO dto){
        Usuario usuario = buscarUsuarioPorUserName(dto.userName().toUpperCase());
        validarCodigo(usuario, dto.codigo());
    }
    @Transactional
    public void alterarSenhaUsuario (UsuarioAlteracaoSenhaDTO dto){
        Usuario usuario = buscarUsuarioPorUserName(dto.userName().toUpperCase());
        validarCodigo(usuario, dto.codigoVerificado());
        usuario.setSenha(dto.novaSenha());
        usuarioRepositorio.save(usuario);
        servicoDeEmail.enviarEmailSenhaAlteradaComSucesso(usuario.getEmail(), usuario.getUsername());
    }
}

