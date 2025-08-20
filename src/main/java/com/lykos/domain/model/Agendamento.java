package com.lykos.domain.model;

import com.lykos.domain.model.enums.ScheduleStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "agendamento")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgendamento;
    
    @ManyToOne
    @JoinColumn(name = "id_pacote", nullable = false)
    private PacoteServico pacote;
    
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;
    
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    
    @Column(nullable = false)
    private LocalDateTime dataFim;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus statusAgendamento = ScheduleStatus.AGENDADO;
    
    @Column(columnDefinition = "TEXT")
    private String motivoCancelamento;
    
    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();
    
    @OneToOne(mappedBy = "agendamento", cascade = CascadeType.ALL)
    private Contratacao contratacao;
}