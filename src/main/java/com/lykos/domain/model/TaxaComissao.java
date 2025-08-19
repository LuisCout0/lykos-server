package com.lykos.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "taxa_comissao")
public class TaxaComissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTaxa;
    
    @Column(nullable = false, unique = true, precision = 5, scale = 2)
    private BigDecimal percentual;
    
    @Column(nullable = false)
    private LocalDateTime dataImplementacao = LocalDateTime.now();
    
    @Column(nullable = false)
    private Boolean ativa = true;
}