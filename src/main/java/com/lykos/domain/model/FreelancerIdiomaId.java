package com.lykos.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class FreelancerIdiomaId implements Serializable {
    private Integer idFreelancer;
    private Integer idIdioma;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreelancerIdiomaId that = (FreelancerIdiomaId) o;
        return Objects.equals(idFreelancer, that.idFreelancer) && 
               Objects.equals(idIdioma, that.idIdioma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFreelancer, idIdioma);
    }
}