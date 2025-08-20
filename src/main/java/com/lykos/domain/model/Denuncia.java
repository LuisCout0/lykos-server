package com.lykos.domain.model;

import com.lykos.domain.model.enums.ReportStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "denuncia")
public class Denuncia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDenuncia;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario_denunciante", nullable = false)
    private Usuario usuarioDenunciante;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario_denunciado", nullable = false)
    private Usuario usuarioDenunciado;
    
    @Column(nullable = false, length = 100)
    private String motivo;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus statusDenuncia = ReportStatus.PENDENTE;
    
    @Column(nullable = false)
    private LocalDateTime dataDenuncia = LocalDateTime.now();
}