package com.lykos.domain.model;

import com.lykos.domain.model.enums.MediaType;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPortfolio;
    
    @ManyToOne
    @JoinColumn(name = "id_freelancer", nullable = false)
    private Freelancer freelancer;
    
    @Column(nullable = false, length = 100)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType tipoMidia;
    
    @Column(nullable = false, length = 255)
    private String urlMidia;
    
    @Column(nullable = false)
    private LocalDateTime dataUpload = LocalDateTime.now();
}