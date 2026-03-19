package com.brunofragadev.usuarios.service;

import com.brunofragadev.email.ServicoDeEmail;
import com.brunofragadev.usuarios.dto.entrada.AtualizarDadosPerfilDTO;
import com.brunofragadev.usuarios.dto.entrada.CadastrarUsuarioDTO;
import com.brunofragadev.usuarios.dto.entrada.UsuarioAlteracaoSenhaDTO;
import com.brunofragadev.usuarios.dto.entrada.ValidarUsuarioDTO;
import com.brunofragadev.usuarios.dto.saida.UsuarioDTO;
import com.brunofragadev.usuarios.dto.saida.UsuarioRecuperacaoSenhaDTO;
import com.brunofragadev.usuarios.validator.UsuarioValidador;
import com.brunofragadev.usuarios.entity.Role;
import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.usuarios.exceptions.*;
import com.brunofragadev.usuarios.mapper.UsuarioMapeador;
import com.brunofragadev.usuarios.repository.UsuarioRepositorio;
import com.brunofragadev.utils.retorno_padrao_api.FormatadoresUteis;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public UsuarioDTO cadastrarUsuario(CadastrarUsuarioDTO dto) {
        usuarioValidador.validarNovoUsuario(dto, false);
        String senhaCriptografada = codificadorSenha.encode(dto.senha());
        Usuario usuario = usuarioMapeador.mapearNovoUsuario(dto, senhaCriptografada);
        usuario.setRole(Role.USER);
        usuario.setContaAtiva(false);
        String codigoGerado = gerarCodigoVerificacao();
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
    public UsuarioDTO atualizarDadosPerfil(String userName, AtualizarDadosPerfilDTO dadosAtualizados){
        Usuario usuario = buscarUsuarioPorUserName(userName.toUpperCase());
        Usuario usuarioAtualizado = usuarioMapeador.mapearDadosAtualizadosPerfil(usuario, dadosAtualizados);
        usuarioRepositorio.save(usuarioAtualizado);
        return usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuarioAtualizado);
    }

    @Transactional
    public void gerarNovoCodigo (String userName){
        Usuario usuario = buscarUsuarioPorUserName(userName.toUpperCase());
        String codigoGerado = gerarCodigoVerificacao();
        usuario.setCodigoVerificacao(codigoGerado);
        usuario.setExpiracaoCodigo(LocalDateTime.now().plusMinutes(5));
        usuarioRepositorio.save(usuario);
        servicoDeEmail.enviarEmailDeVerificacao(usuario.getEmail(), usuario.getUsername(), usuario.getCodigoVerificacao());
    }

    @Transactional
    public UsuarioDTO autenticarContaAtiva(ValidarUsuarioDTO dto){
        Usuario usuario = buscarPorUserNameOuEmail(dto.userName().toUpperCase(), dto.userName().toUpperCase());
        validarCodigo(usuario, dto.codigo());
        usuario.setContaAtiva(true);
        usuario.setCodigoVerificacao(null);
        usuarioRepositorio.save(usuario);
        UsuarioDTO usuarioDTO = usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
        servicoDeEmail.enviarEmailDeBoasVindas(usuarioDTO);
        return usuarioDTO;
    }
    public boolean existePorNomeUsuario(String nomeUsuario) {
        return usuarioRepositorio.existsByUserName(nomeUsuario);
    }
    public UsuarioDTO fornecerDadosUsuarioAutenticado(String userName){
        Usuario usuario = buscarUsuarioPorUserName(userName);
        return usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
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

    private Usuario buscarPorEmail (String email){
        return usuarioRepositorio.findByEmail(email.toUpperCase()).orElseThrow(() -> new UserDontHaveEmailRegistered("Email não localizado"));
    }
    private Usuario buscarPorUserNameOuEmail (String userName, String email){
        return usuarioRepositorio.findByUserNameOrEmail(userName, email).orElseThrow(() -> new UserDontHaveEmailRegistered("Email ou UserName não encontrado no sistema, por favor verifique as informações"));
    }
    private String gerarCodigoVerificacao() {
        return String.format("%06d", new SecureRandom().nextInt(1_000_000));
    }
    @Transactional
    public UsuarioRecuperacaoSenhaDTO enviarCodigoRecuperacaoSenhaPorEmail (String userNameOuEmail){
        Usuario usuario = buscarPorUserNameOuEmail(userNameOuEmail.toUpperCase(), userNameOuEmail.toUpperCase());
        String novoCodigo = gerarCodigoVerificacao();
        usuario.setCodigoVerificacao(novoCodigo);
        usuario.setExpiracaoCodigo(LocalDateTime.now().plusMinutes(5));
        usuarioRepositorio.save(usuario);
        servicoDeEmail.enviarEmailDeRecuperacaoDeSenha(usuario.getEmail(), usuario.getUsername(), novoCodigo);
        return new UsuarioRecuperacaoSenhaDTO(FormatadoresUteis.ofuscarEmail(usuario.getEmail()));
    }
    @Transactional
    public void validarCodigoRecuperacaoSenha (ValidarUsuarioDTO dto){
        Usuario usuario = buscarPorUserNameOuEmail(dto.userName().toUpperCase(), dto.userName().toUpperCase());
        validarCodigo(usuario, dto.codigo());
    }
    @Transactional
    public void alterarSenhaUsuario (UsuarioAlteracaoSenhaDTO dto){
        Usuario usuario = buscarPorUserNameOuEmail(dto.userName().toUpperCase(), dto.userName().toUpperCase());
        validarCodigo(usuario, dto.codigoVerificado());
        String senhaNovaCriptografada = codificadorSenha.encode(dto.novaSenha());
        usuario.setSenha(senhaNovaCriptografada);
        usuario.setCodigoVerificacao(null);
        usuarioRepositorio.save(usuario);
        servicoDeEmail.enviarEmailSenhaAlteradaComSucesso(usuario.getEmail(), usuario.getUsername());
    }
    public UsuarioDTO validarCredenciais(String userNameOuEmail, String senha) {
        Usuario usuario = buscarPorUserNameOuEmail(userNameOuEmail.toUpperCase(), userNameOuEmail.toUpperCase());
        if (!codificadorSenha.matches(senha, usuario.getSenha())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }
        return usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
    }
    @Transactional
    public UsuarioDTO processarLoginGoogle(String email, String nome, String fotoUrl) {
        String emailFormatado = email.toUpperCase();
        Optional<Usuario> usuarioExistente = usuarioRepositorio.findByEmail(emailFormatado);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            if (fotoUrl != null && !fotoUrl.equals(usuario.getFotoperfil())) {
                usuario.setFotoperfil(fotoUrl);
            }
            if(!usuario.isContaAtiva()){
                usuario.setContaAtiva(true);
            }
            return usuarioMapeador.mapearUsuarioParaUsuarioDTO(usuario);
        }
        String userNameFinal = email.split("@")[0].toUpperCase();
        if (existePorNomeUsuario(userNameFinal)) {
            userNameFinal = userNameFinal + "_" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        }
        String senhaCriptografada = codificadorSenha.encode(UUID.randomUUID().toString());

        Usuario novoUsuario = usuarioMapeador.mapearNovoUsuarioSocialGoogle(
                emailFormatado, nome, fotoUrl, senhaCriptografada, userNameFinal
        );
        usuarioRepositorio.save(novoUsuario);
        UsuarioDTO novoUsuarioDTO = usuarioMapeador.mapearUsuarioParaUsuarioDTO(novoUsuario);
        servicoDeEmail.enviarEmailDeBoasVindas(novoUsuarioDTO);
        return novoUsuarioDTO;
    }
}

