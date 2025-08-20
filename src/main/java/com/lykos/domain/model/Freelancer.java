package com.lykos.domain.model;

import com.lykos.domain.model.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "freelancer")
public class Freelancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFreelancer;
    
    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;
    
    @Column(nullable = false, length = 50)
    private String nomeExibicao;
    
    @Column(columnDefinition = "TEXT")
    private String descricaoPerfil;
    
    @Column(length = 255)
    private String fotoPerfilUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileStatus statusPerfil = ProfileStatus.ATIVO;
    
    @Column(nullable = false)
    private LocalDateTime dataInicioNaPlataforma = LocalDateTime.now();
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<FreelancerHabilidade> habilidades;
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<FreelancerIdioma> idiomas;
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<PacoteServico> pacotesServico;
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<Portfolio> portfolio;
}