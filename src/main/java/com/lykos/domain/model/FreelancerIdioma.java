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
}

@Embeddable
class FreelancerIdiomaId implements java.io.Serializable {
    private Long idFreelancer;
    private Long idIdioma;
}