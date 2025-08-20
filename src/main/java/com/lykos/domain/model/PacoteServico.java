package com.lykos.domain.model;

import com.lykos.domain.model.enums.PackageStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pacote_servico")
public class PacoteServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPacote;
    
    @ManyToOne
    @JoinColumn(name = "id_freelancer", nullable = false)
    private Freelancer freelancer;
    
    @Column(nullable = false, length = 100)
    private String titulo;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @Column(nullable = false)
    private Integer prazoEntregaDias;
    
    @Column(nullable = false)
    private Integer numeroRevisoes = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageStatus statusPacote = PackageStatus.ATIVO;
    
    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @OneToMany(mappedBy = "pacote", cascade = CascadeType.ALL)
    private List<PacoteCategoria> categorias;
    
    @OneToMany(mappedBy = "pacote", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;
}