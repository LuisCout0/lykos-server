package com.lykos.domain.repository;

import com.lykos.domain.model.Freelancer;
import com.lykos.domain.model.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Integer> {

    Optional<Freelancer> findByUsuarioIdUsuario(Integer idUsuario);
    
    Optional<Freelancer> findByNomeExibicao(String nomeExibicao);
    
    List<Freelancer> findByStatusPerfil(ProfileStatus statusPerfil);
    
    List<Freelancer> findByNomeExibicaoContainingIgnoreCase(String nome);
    
    List<Freelancer> findByDescricaoPerfilContainingIgnoreCase(String descricao);
    
    @Query("SELECT f FROM Freelancer f JOIN f.habilidades fh WHERE fh.habilidade.nome LIKE %:habilidade%")
    List<Freelancer> findByHabilidadeNome(@Param("habilidade") String habilidade);
    
    @Query("SELECT f FROM Freelancer f JOIN f.idiomas fi WHERE fi.idioma.nome LIKE %:idioma%")
    List<Freelancer> findByIdiomaNome(@Param("idioma") String idioma);
    
    @Query("SELECT f FROM Freelancer f JOIN f.idiomas fi WHERE fi.nivelProficiencia = :nivel")
    List<Freelancer> findByNivelIdioma(@Param("nivel") String nivel);
    
    @Query("SELECT f FROM Freelancer f WHERE f.usuario.email = :email")
    Optional<Freelancer> findByUsuarioEmail(@Param("email") String email);
    
    @Query("SELECT f FROM Freelancer f JOIN f.habilidades fh WHERE fh.habilidade.idHabilidade = :idHabilidade")
    List<Freelancer> findByHabilidadeId(@Param("idHabilidade") Integer idHabilidade);
    
    @Query("SELECT f FROM Freelancer f JOIN f.idiomas fi WHERE fi.idioma.idIdioma = :idIdioma")
    List<Freelancer> findByIdiomaId(@Param("idIdioma") Integer idIdioma);
    
    boolean existsByNomeExibicao(String nomeExibicao);
    
    Integer countByStatusPerfil(ProfileStatus statusPerfil);
}