package com.lykos.domain.model;

import com.lykos.domain.model.enums.UserType;
import com.lykos.domain.model.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 50)
    private String nomeUsuario;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String senhaHash;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    private LocalDateTime ultimoLogin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "user_type")
    private UserType tipoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "account_status")
    private AccountStatus statusConta = AccountStatus.ATIVO;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Freelancer freelancer;

    @OneToMany(mappedBy = "usuarioDenunciante", cascade = CascadeType.ALL)
    private List<Denuncia> denunciasFeitas;

    @OneToMany(mappedBy = "usuarioDenunciado", cascade = CascadeType.ALL)
    private List<Denuncia> denunciasRecebidas;

    // ========== MÉTODOS DE DOMÍNIO ==========

    // Construtor para criação de novo usuário
    public Usuario(String nomeCompleto, String nomeUsuario, String email,
                   String senhaHash, UserType tipoUsuario) {
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senhaHash = senhaHash;
        this.tipoUsuario = tipoUsuario;
        this.dataCadastro = LocalDateTime.now();
        this.statusConta = AccountStatus.ATIVO;
    }

    protected Usuario() {
        // Construtor padrão para JPA
    }

    // Método para atualizar último login
    public void registrarLogin() {
        this.ultimoLogin = LocalDateTime.now();
    }

    // Método para verificar se a conta está ativa
    public boolean isAtivo() {
        return this.statusConta == AccountStatus.ATIVO;
    }

    // Método para verificar se é freelancer
    public boolean isFreelancer() {
        return this.tipoUsuario == UserType.FREELANCER;
    }

    // Método para verificar se é cliente
    public boolean isCliente() {
        return this.tipoUsuario == UserType.CLIENTE;
    }

    // Método para ativar conta
    public void ativarConta() {
        this.statusConta = AccountStatus.ATIVO;
    }

    // Método para desativar conta
    public void desativarConta() {
        this.statusConta = AccountStatus.INATIVO;
    }

    // Método para banir conta
    public void banirConta() {
        this.statusConta = AccountStatus.BANIDO;
    }

    // Método para atualizar email (com validação)
    public void atualizarEmail(String novoEmail) {
        if (novoEmail == null || novoEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        if (!novoEmail.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = novoEmail.toLowerCase();
    }

    // Método para atualizar telefone
    public void atualizarTelefone(String novoTelefone) {
        // Remover caracteres não numéricos
        if (novoTelefone != null) {
            this.telefone = novoTelefone.replaceAll("[^0-9]", "");
        } else {
            this.telefone = null;
        }
    }

    // Método para verificar se usuário está inativo há muito tempo
    public boolean isInativoPor(Integer meses) {
        if (ultimoLogin == null) {
            return dataCadastro.isBefore(LocalDateTime.now().minusMonths(meses));
        }
        return ultimoLogin.isBefore(LocalDateTime.now().minusMonths(meses));
    }

    // Método para converter para freelancer (se ainda não for)
    public void converterParaFreelancer() {
        if (!isFreelancer()) {
            this.tipoUsuario = UserType.FREELANCER;
            // Aqui você pode criar automaticamente o perfil freelancer
            if (this.freelancer == null) {
                this.freelancer = new Freelancer();
                this.freelancer.setUsuario(this);
                this.freelancer.setNomeExibicao(this.nomeCompleto);
            }
        }
    }

    // Método para validar se o usuário pode ser salvo
    public boolean isValid() {
        return nomeCompleto != null && !nomeCompleto.trim().isEmpty() &&
                email != null && email.contains("@") &&
                senhaHash != null && senhaHash.length() >= 6;
    }
}