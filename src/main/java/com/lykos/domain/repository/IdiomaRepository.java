package com.lykos.domain.repository;

import com.lykos.domain.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdiomaRepository extends JpaRepository<Idioma, Integer> {
    Optional<Idioma> findByNome(String nome);
    Optional<Idioma> findByCodigoIso(String codigoIso);
    List<Idioma> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNome(String nome);
    boolean existsByCodigoIso(String codigoIso);
}