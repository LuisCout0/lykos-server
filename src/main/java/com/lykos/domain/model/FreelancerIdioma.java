package com.lykos.domain.model;

import com.lykos.domain.model.enums.LanguageLevel;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "freelancer_idioma")
public class FreelancerIdioma {
    
    @EmbeddedId
    private FreelancerIdiomaId id;
    
    @ManyToOne
    @MapsId("idFreelancer")
    @JoinColumn(name = "id_freelancer")
    private Freelancer freelancer;
    
    @ManyToOne
    @MapsId("idIdioma")
    @JoinColumn(name = "id_idioma")
    private Idioma idioma;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageLevel nivelProficiencia;
    
    // Construtor padrão para JPA
    protected FreelancerIdioma() {}
    
    // Construtor de domínio
    public FreelancerIdioma(Freelancer freelancer, Idioma idioma, LanguageLevel nivelProficiencia) {
        this.freelancer = freelancer;
        this.idioma = idioma;
        this.nivelProficiencia = nivelProficiencia;
        
        this.id = new FreelancerIdiomaId();
        this.id.setIdFreelancer(freelancer.getIdFreelancer().intValue());
        this.id.setIdIdioma(idioma.getIdIdioma().intValue());
    }
}