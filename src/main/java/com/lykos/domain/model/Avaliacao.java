package com.lykos.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "avaliacao")
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvaliacao;
    
    @OneToOne
    @JoinColumn(name = "id_contratacao", nullable = false)
    private Contratacao contratacao;
    
    @Column(nullable = false)
    private Integer nota;
    
    @Column(columnDefinition = "TEXT")
    private String comentario;
    
    @Column(nullable = false)
    private LocalDateTime dataAvaliacao = LocalDateTime.now();
}