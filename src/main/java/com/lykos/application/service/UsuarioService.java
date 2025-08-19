package com.lykos.application.service;

import com.lykos.domain.model.Usuario;
import com.lykos.domain.model.enums.AccountStatus;
import com.lykos.domain.model.enums.UserType;
import com.lykos.domain.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;

    // ========== MÉTODOS DE CRIAÇÃO ==========

    public Usuario criarUsuario(String nomeCompleto, String nomeUsuario,
                                String email, String senha, UserType tipoUsuario) {

        validarDisponibilidadeEmail(email);
        validarDisponibilidadeNomeUsuario(nomeUsuario);

        if (!passwordService.isPasswordStrong(senha)) {
            throw new RuntimeException("Senha deve ter pelo menos 8 caracteres, incluindo letras, números e símbolos");
        }

        String senhaHash = passwordService.encode(senha);
        Usuario usuario = new Usuario(nomeCompleto, nomeUsuario, email, senhaHash, tipoUsuario);

        return usuarioRepository.save(usuario);
    }

    public Usuario criarUsuarioComSenhaTemporaria(String nomeCompleto, String nomeUsuario,
                                                  String email, UserType tipoUsuario) {

        validarDisponibilidadeEmail(email);
        validarDisponibilidadeNomeUsuario(nomeUsuario);

        String senhaTemporaria = passwordService.generateRandomPassword();
        String senhaHash = passwordService.encode(senhaTemporaria);

        Usuario usuario = new Usuario(nomeCompleto, nomeUsuario, email, senhaHash, tipoUsuario);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Aqui você poderia enviar a senha temporária por email
        System.out.println("Senha temporária para " + email + ": " + senhaTemporaria);

        return usuarioSalvo;
    }

    // ========== MÉTODOS DE AUTENTICAÇÃO ==========

    public Optional<Usuario> autenticar(String login, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(login)
                .or(() -> usuarioRepository.findByNomeUsuario(login));

        if (usuario.isPresent() &&
                passwordService.matches(senha, usuario.get().getSenhaHash())) {
            usuario.get().registrarLogin();
            usuarioRepository.save(usuario.get());
            return usuario;
        }

        return Optional.empty();
    }

    public boolean verificarSenha(Long userId, String senha) {
        return usuarioRepository.findById(userId)
                .map(usuario -> passwordService.matches(senha, usuario.getSenhaHash()))
                .orElse(false);
    }

    // ========== MÉTODOS DE ALTERAÇÃO DE SENHA ==========

    @Transactional
    public void atualizarSenha(Long userId, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarPorIdOuFalhar(userId);

        if (!passwordService.matches(senhaAtual, usuario.getSenhaHash())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        if (!passwordService.isPasswordStrong(novaSenha)) {
            throw new RuntimeException("Nova senha deve ter pelo menos 8 caracteres, incluindo letras, números e símbolos");
        }

        String novaSenhaHash = passwordService.encode(novaSenha);
        usuario.setSenhaHash(novaSenhaHash);
    }

    @Transactional
    public void redefinirSenha(Long userId, String novaSenha) {
        Usuario usuario = buscarPorIdOuFalhar(userId);

        if (!passwordService.isPasswordStrong(novaSenha)) {
            throw new RuntimeException("Nova senha deve ter pelo menos 8 caracteres, incluindo letras, números e símbolos");
        }

        String novaSenhaHash = passwordService.encode(novaSenha);
        usuario.setSenhaHash(novaSenhaHash);
    }

    @Transactional
    public void gerarSenhaTemporaria(Long userId) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        String senhaTemporaria = passwordService.generateRandomPassword();
        String senhaHash = passwordService.encode(senhaTemporaria);

        usuario.setSenhaHash(senhaHash);

        // Aqui você enviaria a senha temporária por email
        System.out.println("Nova senha temporária para " + usuario.getEmail() + ": " + senhaTemporaria);
    }

    // ========== MÉTODOS DE ALTERAÇÃO DE ESTADO ==========

    @Transactional
    public void registrarLogin(Long userId) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        usuario.registrarLogin();
    }

    @Transactional
    public void ativarConta(Long userId) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        usuario.ativarConta();
    }

    @Transactional
    public void desativarConta(Long userId) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        usuario.desativarConta();
    }

    @Transactional
    public void banirConta(Long userId) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        usuario.banirConta();
    }

    @Transactional
    public void atualizarEmail(Long userId, String novoEmail) {
        Usuario usuario = buscarPorIdOuFalhar(userId);

        if (usuarioRepository.existsByEmailAndIdUsuarioNot(novoEmail, userId)) {
            throw new RuntimeException("Email já está em uso por outro usuário");
        }

        usuario.atualizarEmail(novoEmail);
    }

    @Transactional
    public void atualizarTelefone(Long userId, String novoTelefone) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        usuario.atualizarTelefone(novoTelefone);
    }

    @Transactional
    public void atualizarNomeCompleto(Long userId, String novoNome) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        usuario.setNomeCompleto(novoNome);
    }

    @Transactional
    public void atualizarNomeUsuario(Long userId, String novoNomeUsuario) {
        Usuario usuario = buscarPorIdOuFalhar(userId);

        if (usuarioRepository.existsByNomeUsuarioAndIdUsuarioNot(novoNomeUsuario, userId)) {
            throw new RuntimeException("Nome de usuário já está em uso");
        }

        usuario.setNomeUsuario(novoNomeUsuario);
    }

    @Transactional
    public void converterParaFreelancer(Long userId) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        usuario.converterParaFreelancer();
    }

    // ========== MÉTODOS DE CONSULTA ==========

    public Optional<Usuario> buscarPorId(Long userId) {
        return usuarioRepository.findById(userId);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarFreelancersAtivos() {
        return usuarioRepository.findByTipoUsuarioAndStatusConta(
                UserType.FREELANCER, AccountStatus.ATIVO);
    }

    public List<Usuario> listarClientesAtivos() {
        return usuarioRepository.findByTipoUsuarioAndStatusConta(
                UserType.CLIENTE, AccountStatus.ATIVO);
    }

    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeCompletoContainingIgnoreCase(nome);
    }

    public List<Usuario> buscarPorNomeUsuario(String nomeUsuario) {
        return usuarioRepository.findByNomeUsuarioContainingIgnoreCase(nomeUsuario);
    }

    public List<Usuario> listarPorStatus(AccountStatus status) {
        return usuarioRepository.findByStatusConta(status);
    }

    public List<Usuario> listarPorTipo(UserType tipo) {
        return usuarioRepository.findByTipoUsuario(tipo);
    }

    public List<Usuario> listarUsuariosInativosPorMeses(Long meses) {
        LocalDateTime dataLimite = LocalDateTime.now().minusMonths(meses);
        return usuarioRepository.findByStatusContaAndUltimoLoginBefore(
                AccountStatus.ATIVO, dataLimite);
    }

    public List<Usuario> listarUsuariosSemLogin() {
        return usuarioRepository.findByUltimoLoginIsNull();
    }

    public boolean isUsuarioAtivo(Long userId) {
        return usuarioRepository.findById(userId)
                .map(Usuario::isAtivo)
                .orElse(false);
    }

    public boolean isFreelancer(Long userId) {
        return usuarioRepository.findById(userId)
                .map(Usuario::isFreelancer)
                .orElse(false);
    }

    public boolean isCliente(Long userId) {
        return usuarioRepository.findById(userId)
                .map(Usuario::isCliente)
                .orElse(false);
    }

    public boolean isInativoPor(Long userId, Long meses) {
        return usuarioRepository.findById(userId)
                .map(usuario -> usuario.isInativoPor(meses))
                .orElse(false);
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existeNomeUsuario(String nomeUsuario) {
        return usuarioRepository.existsByNomeUsuario(nomeUsuario);
    }

//    public long contarPorTipo(UserType tipo) {
//        return usuarioRepository.countByTipoUsuario(tipo);
//    }

    public long contarPorStatus(AccountStatus status) {
        return usuarioRepository.countByStatusConta(status);
    }

    public long contarTotalUsuarios() {
        return usuarioRepository.count();
    }

    // ========== MÉTODOS UTILITÁRIOS PRIVADOS ==========

    private Usuario buscarPorIdOuFalhar(Long userId) {
        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));
    }

    private void validarDisponibilidadeEmail(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já cadastrado");
        }
    }

    private void validarDisponibilidadeNomeUsuario(String nomeUsuario) {
        if (usuarioRepository.existsByNomeUsuario(nomeUsuario)) {
            throw new RuntimeException("Nome de usuário já existe");
        }
    }

    // ========== MÉTODOS DE ADMINISTRAÇÃO ==========

    @Transactional
    public void desativarUsuariosInativos(int mesesInatividade) {
        List<Usuario> usuarios = usuarioRepository.findByStatusConta(AccountStatus.ATIVO);

        usuarios.stream()
                .filter(usuario -> usuario.isInativoPor((long) mesesInatividade))
                .forEach(Usuario::desativarConta);
    }

    @Transactional
    public void reativarConta(Long userId) {
        Usuario usuario = buscarPorIdOuFalhar(userId);
        if (usuario.getStatusConta() == AccountStatus.BANIDO) {
            throw new RuntimeException("Contas banidas não podem ser reativadas");
        }
        usuario.ativarConta();
    }

    @Transactional
    public void deletarUsuario(Long userId) {
        if (!usuarioRepository.existsById(userId)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(userId);
    }

    @Transactional
    public void deletarUsuariosInativos(int mesesInatividade) {
        List<Usuario> usuarios = usuarioRepository.findByStatusConta(AccountStatus.INATIVO);

        usuarios.stream()
                .filter(usuario -> usuario.isInativoPor((long) mesesInatividade))
                .forEach(usuario -> usuarioRepository.delete(usuario));
    }
}