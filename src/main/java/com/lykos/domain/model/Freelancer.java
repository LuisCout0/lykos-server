package com.lykos.domain.model;

import com.lykos.domain.model.enums.LanguageLevel;
import com.lykos.domain.model.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "freelancer")
public class Freelancer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFreelancer;
    
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
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreelancerHabilidade> habilidades = new ArrayList<>();
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreelancerIdioma> idiomas = new ArrayList<>();
    
    // ========== MÉTODOS DE DOMÍNIO ==========
    
    public void atualizarPerfil(String nomeExibicao, String descricaoPerfil) {
        if (nomeExibicao != null && !nomeExibicao.trim().isEmpty()) {
            this.nomeExibicao = nomeExibicao;
        }
        if (descricaoPerfil != null) {
            this.descricaoPerfil = descricaoPerfil;
        }
    }
    
    public void atualizarFotoPerfil(String fotoUrl) {
        this.fotoPerfilUrl = fotoUrl;
    }
    
    public void ativarPerfil() {
        this.statusPerfil = ProfileStatus.ATIVO;
    }
    
    public void desativarPerfil() {
        this.statusPerfil = ProfileStatus.OCULTO;
    }
    
    public void suspenderPerfil() {
        this.statusPerfil = ProfileStatus.SUSPENSO;
    }
    
    public boolean isAtivo() {
        return this.statusPerfil == ProfileStatus.ATIVO;
    }
    
    public boolean possuiHabilidade(Habilidade habilidade) {
        return habilidades.stream()
                .anyMatch(fh -> fh.getHabilidade().equals(habilidade));
    }
    
    public boolean possuiIdioma(Idioma idioma) {
        return idiomas.stream()
                .anyMatch(fi -> fi.getIdioma().equals(idioma));
    }
    
    public void adicionarHabilidade(Habilidade habilidade) {
        if (!possuiHabilidade(habilidade)) {
            FreelancerHabilidade freelancerHabilidade = new FreelancerHabilidade(this, habilidade);
            habilidades.add(freelancerHabilidade);
        }
    }
    
    public void removerHabilidade(Habilidade habilidade) {
        habilidades.removeIf(fh -> fh.getHabilidade().equals(habilidade));
    }
    
    public void adicionarIdioma(Idioma idioma, LanguageLevel nivel) {
        if (!possuiIdioma(idioma)) {
            FreelancerIdioma freelancerIdioma = new FreelancerIdioma(this, idioma, nivel);
            idiomas.add(freelancerIdioma);
        }
    }
    
    public void removerIdioma(Idioma idioma) {
        idiomas.removeIf(fi -> fi.getIdioma().equals(idioma));
    }
    
    public void atualizarNivelIdioma(Idioma idioma, LanguageLevel novoNivel) {
        idiomas.stream()
                .filter(fi -> fi.getIdioma().equals(idioma))
                .findFirst()
                .ifPresent(fi -> fi.setNivelProficiencia(novoNivel));
    }
}