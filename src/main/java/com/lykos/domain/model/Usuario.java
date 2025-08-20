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
    private Long idUsuario;
    
    @Column(nullable = false, length = 100)
    private String nomeCompleto;
    
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
    @Column(nullable = false)
    private UserType tipoUsuario;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus statusConta = AccountStatus.ATIVO;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;
    
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Freelancer freelancer;
    
    @OneToMany(mappedBy = "usuarioDenunciante", cascade = CascadeType.ALL)
    private List<Denuncia> denunciasFeitas;
    
    @OneToMany(mappedBy = "usuarioDenunciado", cascade = CascadeType.ALL)
    private List<Denuncia> denunciasRecebidas;
}