package com.lykos.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "freelancer_habilidade")
public class FreelancerHabilidade {
    
    @EmbeddedId
    private FreelancerHabilidadeId id;
    
    @ManyToOne
    @MapsId("idFreelancer")
    @JoinColumn(name = "id_freelancer")
    private Freelancer freelancer;
    
    @ManyToOne
    @MapsId("idHabilidade")
    @JoinColumn(name = "id_habilidade")
    private Habilidade habilidade;
    
    // Construtor padrão para JPA
    protected FreelancerHabilidade() {}
    
    // Construtor de domínio
    public FreelancerHabilidade(Freelancer freelancer, Habilidade habilidade) {
        this.freelancer = freelancer;
        this.habilidade = habilidade;
        
        this.id = new FreelancerHabilidadeId();
        this.id.setIdFreelancer(freelancer.getIdFreelancer().intValue());
        this.id.setIdHabilidade(habilidade.getIdHabilidade());
    }
}