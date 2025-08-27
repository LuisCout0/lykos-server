package com.lykos.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@AllArgsConstructor @NoArgsConstructor
public class PacoteCategoriaId implements java.io.Serializable {
    private Integer idPacote;
    private Integer idSubcategoria;
}