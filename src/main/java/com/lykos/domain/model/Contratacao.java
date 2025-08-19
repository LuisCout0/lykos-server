package com.lykos.domain.model;

import com.lykos.domain.model.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "contratacao")
public class Contratacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContratacao;
    
    @OneToOne
    @JoinColumn(name = "id_agendamento", nullable = false)
    private Agendamento agendamento;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaPlataforma;
    
    @Column(nullable = false, length = 50)
    private String metodoPagamento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus statusPagamento = PaymentStatus.PENDENTE;
    
    private LocalDateTime dataPagamento;
    
    @Column(nullable = false)
    private Boolean confirmadoPorCliente = false;
    
    @OneToOne(mappedBy = "contratacao", cascade = CascadeType.ALL)
    private Avaliacao avaliacao;
}