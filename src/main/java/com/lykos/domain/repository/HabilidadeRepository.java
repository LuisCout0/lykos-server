package com.lykos.domain.repository;

import com.lykos.domain.model.Habilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Integer> {
    Optional<Habilidade> findByNome(String nome);
    List<Habilidade> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNome(String nome);
}