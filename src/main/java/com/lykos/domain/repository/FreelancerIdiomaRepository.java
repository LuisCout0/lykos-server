package com.lykos.domain.repository;

import com.lykos.domain.model.Freelancer;
import com.lykos.domain.model.FreelancerIdioma;
import com.lykos.domain.model.FreelancerIdiomaId;
import com.lykos.domain.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerIdiomaRepository extends JpaRepository<FreelancerIdioma, FreelancerIdiomaId> {
    
    boolean existsByFreelancerAndIdioma(Freelancer freelancer, Idioma idioma);
    
    void deleteByFreelancerAndIdioma(Freelancer freelancer, Idioma idioma);
    
    List<FreelancerIdioma> findByFreelancer(Freelancer freelancer);
    
    @Query("SELECT fi.idioma FROM FreelancerIdioma fi WHERE fi.freelancer = :freelancer")
    List<Idioma> findIdiomasByFreelancer(@Param("freelancer") Freelancer freelancer);
    
    Optional<FreelancerIdioma> findByFreelancerAndIdioma(Freelancer freelancer, Idioma idioma);
    
    @Query("SELECT fi FROM FreelancerIdioma fi WHERE fi.freelancer = :freelancer AND fi.nivelProficiencia = :nivel")
    List<FreelancerIdioma> findByFreelancerAndNivel(@Param("freelancer") Freelancer freelancer, 
                                                   @Param("nivel") String nivel);
}