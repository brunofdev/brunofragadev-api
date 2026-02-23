package com.brunofragadev.usuarios.controller;

import com.brunofragadev.autenticacao.dto.UsuarioLoginResponseDTO;
import com.brunofragadev.autenticacao.service.AuthenticationService;
import com.brunofragadev.configs.JwtProvider;
import com.brunofragadev.usuarios.dto.*;
import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.usuarios.service.UsuarioServico;
import com.brunofragadev.utils.retorno_padrao_api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/usuario")
@Validated
public class UsuarioController {


    @Autowired
    private UsuarioServico usuarioServico;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<UsuarioDTO>> cadastrarUsuario(@Valid @RequestBody CadastrarUsuarioDTO dto){
        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        CadastrarUsuarioDTO dtoComSenhaEncoded = dto.withSenha(senhaCriptografada);
        UsuarioDTO usuarioCriadoDTO = usuarioServico.cadastrarUsuario(dtoComSenhaEncoded);
        return ResponseEntity.ok(ApiResponse.success("Recurso criado" , usuarioCriadoDTO));
    }
    @GetMapping("/obter-todos")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> listarUsuarios(){
        return ResponseEntity.ok(ApiResponse.success("Recurso disponivel", usuarioServico.listarUsuarios()));
    }
    @PostMapping("/ativar-conta")
    public ResponseEntity<ApiResponse<UsuarioLoginResponseDTO>> ativarConta(@RequestBody @Valid AutenticarUsuarioDTO dto) {
        UsuarioDTO usuarioAtivado = usuarioServico.autenticarContaAtiva(dto);
        String token = jwtProvider.generateToken(usuarioAtivado.userName(), usuarioAtivado.role());
        return ResponseEntity.ok(ApiResponse.success("Recurso disponivel", new UsuarioLoginResponseDTO(token, usuarioAtivado)));
    }
    @GetMapping("/meus-dados")
    public ResponseEntity<ApiResponse<UsuarioDTO>> getUserDetailsByUsername(@AuthenticationPrincipal Usuario usuarioAutenticado){
        UsuarioDTO UserDTO = usuarioServico.fornecerDadosUsuarioAutenticado(usuarioAutenticado.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Dados do usario autenticado", UserDTO));
    }

    @PostMapping("/reenviar-codigo")
    public ResponseEntity<Void> reenviarCodigo(@RequestBody String userName) {
        usuarioServico.gerarNovoCodigo(userName);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/senha/recuperacao")
    public ResponseEntity<ApiResponse<UsuarioRecuperacaoSenhaDTO>> recuperarSenha(@RequestBody String userNameOuEmail) {
        UsuarioRecuperacaoSenhaDTO emailDTO = usuarioServico.enviarCodigoRecuperacaoSenhaPorEmail(userNameOuEmail);
        return ResponseEntity.ok(ApiResponse.success("Email enviado com sucesso", emailDTO));
    }
    @PostMapping("/senha/recuperacao/validar-codigo")
    public ResponseEntity<Void> validarCodigoAlteracaoSenha(@RequestBody AutenticarUsuarioDTO dto){
        usuarioServico.validarCodigoRecuperacaoSenha(dto);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/senha/recuperacao/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid UsuarioAlteracaoSenhaDTO dto) {
        String senhaCodificada = passwordEncoder.encode(dto.novaSenha());
        UsuarioAlteracaoSenhaDTO dtoSenhaCodificada = dto.withNovaSenha(senhaCodificada);
        usuarioServico.alterarSenhaUsuario(dtoSenhaCodificada);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/meus-dados/atualizar")
    public ResponseEntity<ApiResponse<UsuarioDTO>> atualizarPerfil(
            @AuthenticationPrincipal Usuario usuarioAutenticado,
            @Valid @RequestBody AtualizarDadosPerfilDTO dto) {
        UsuarioDTO perfilAtualizado = usuarioServico.atualizarDadosPerfil(usuarioAutenticado.getUsername(), dto);
        return ResponseEntity.ok(ApiResponse.success("Perfil atualizado com sucesso", perfilAtualizado));
    }
}

