package com.lykos.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "habilidade")
public class Habilidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabilidade;
    
    @Column(nullable = false, length = 50)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @OneToMany(mappedBy = "habilidade", cascade = CascadeType.ALL)
    private List<FreelancerHabilidade> freelancers;
}