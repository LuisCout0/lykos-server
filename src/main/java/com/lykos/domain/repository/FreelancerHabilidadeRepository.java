package com.lykos.domain.repository;

import com.lykos.domain.model.Freelancer;
import com.lykos.domain.model.FreelancerHabilidade;
import com.lykos.domain.model.FreelancerHabilidadeId;
import com.lykos.domain.model.Habilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerHabilidadeRepository extends JpaRepository<FreelancerHabilidade, FreelancerHabilidadeId> {
    
    boolean existsByFreelancerAndHabilidade(Freelancer freelancer, Habilidade habilidade);
    
    void deleteByFreelancerAndHabilidade(Freelancer freelancer, Habilidade habilidade);
    
    List<FreelancerHabilidade> findByFreelancer(Freelancer freelancer);
    
    @Query("SELECT fh.habilidade FROM FreelancerHabilidade fh WHERE fh.freelancer = :freelancer")
    List<Habilidade> findHabilidadesByFreelancer(@Param("freelancer") Freelancer freelancer);
    
    Optional<FreelancerHabilidade> findByFreelancerAndHabilidade(Freelancer freelancer, Habilidade habilidade);
}