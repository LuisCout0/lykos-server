package com.lykos.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "idioma")
public class Idioma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIdioma;
    
    @Column(nullable = false, length = 50)
    private String nome;
    
    @Column(nullable = false, length = 5)
    private String codigoIso;
    
    @OneToMany(mappedBy = "idioma", cascade = CascadeType.ALL)
    private List<FreelancerIdioma> freelancers;
}