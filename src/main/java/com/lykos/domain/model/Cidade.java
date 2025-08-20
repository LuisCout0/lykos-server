package com.lykos.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cidade")
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCidade;
    
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
    
    @Column(nullable = false, length = 50)
    private String nome;
    
    @OneToMany(mappedBy = "cidade", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;
}