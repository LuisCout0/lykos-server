package com.lykos.api.controller;

import com.lykos.application.service.UsuarioService;
import com.lykos.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuario = usuarioService.autenticar(
                request.getLogin(), request.getSenha());

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = usuarioService.criarUsuario(
                    request.getNomeCompleto(),
                    request.getNomeUsuario(),
                    request.getEmail(),
                    request.getSenha(),
                    request.getTipoUsuario()
            );
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Records para os DTOs de request
    public record LoginRequest(String login, String senha) {}
    public record RegistroRequest(String nomeCompleto, String nomeUsuario,
                                  String email, String senha, String tipoUsuario) {}
}