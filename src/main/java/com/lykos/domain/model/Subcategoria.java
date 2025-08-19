package com.lykos.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subcategoria")
public class Subcategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSubcategoria;
    
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
    
    @Column(nullable = false, length = 50)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @OneToMany(mappedBy = "subcategoria", cascade = CascadeType.ALL)
    private List<PacoteCategoria> pacotes;
}