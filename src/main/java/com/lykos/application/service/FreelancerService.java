package com.lykos.application.service;

import com.lykos.domain.model.*;
import com.lykos.domain.model.enums.LanguageLevel;
import com.lykos.domain.model.enums.ProfileStatus;
import com.lykos.domain.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    private final FreelancerRepository freelancerRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabilidadeRepository habilidadeRepository;
    private final IdiomaRepository idiomaRepository;
    private final StorageService storageService;

    // ========== MÉTODOS BÁSICOS ==========
    
    public Optional<Freelancer> buscarPorId(Integer id) {
        return freelancerRepository.findById(id);
    }

    public Optional<Freelancer> buscarPorIdUsuario(Integer idUsuario) {
        return freelancerRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<Freelancer> listarTodos() {
        return freelancerRepository.findAll();
    }

    public List<Freelancer> buscarPorNome(String nome) {
        return freelancerRepository.findByNomeExibicaoContainingIgnoreCase(nome);
    }

    public List<Freelancer> buscarPorDescricao(String descricao) {
        return freelancerRepository.findByDescricaoPerfilContainingIgnoreCase(descricao);
    }

    public List<Freelancer> buscarPorStatus(ProfileStatus status) {
        return freelancerRepository.findByStatusPerfil(status);
    }

    @Transactional
    public Freelancer criarFreelancer(Integer idUsuario, String nomeExibicao, String descricaoPerfil) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuario));

        if (freelancerRepository.existsByNomeExibicao(nomeExibicao)) {
            throw new RuntimeException("Nome de exibição já está em uso: " + nomeExibicao);
        }

        Freelancer freelancer = new Freelancer();
        freelancer.setUsuario(usuario);
        freelancer.setNomeExibicao(nomeExibicao);
        freelancer.setDescricaoPerfil(descricaoPerfil);
        freelancer.setDataInicioNaPlataforma(LocalDateTime.now());

        return freelancerRepository.save(freelancer);
    }

    @Transactional
    public Freelancer atualizarPerfil(Integer idFreelancer, String nomeExibicao, String descricaoPerfil) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        freelancer.atualizarPerfil(nomeExibicao, descricaoPerfil);
        return freelancer;
    }

    @Transactional
    public void atualizarStatus(Integer idFreelancer, ProfileStatus status) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        
        switch (status) {
            case ATIVO -> freelancer.ativarPerfil();
            case OCULTO -> freelancer.desativarPerfil();
            case SUSPENSO -> freelancer.suspenderPerfil();
        }
    }

    @Transactional
    public Freelancer atualizarFotoPerfil(Integer idFreelancer, byte[] foto, String fileName) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        String fotoUrl = storageService.uploadFile(foto, fileName, "profiles");
        freelancer.atualizarFotoPerfil(fotoUrl);
        return freelancer;
    }

    @Transactional
    public void deletarFreelancer(Integer idFreelancer) {
        if (!freelancerRepository.existsById(idFreelancer)) {
            throw new RuntimeException("Freelancer não encontrado: " + idFreelancer);
        }
        freelancerRepository.deleteById(idFreelancer);
    }

    // ========== MÉTODOS DE HABILIDADES ==========

    @Transactional
    public void adicionarHabilidade(Integer idFreelancer, Integer idHabilidade) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        Habilidade habilidade = habilidadeRepository.findById(idHabilidade)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada: " + idHabilidade));
        
        freelancer.adicionarHabilidade(habilidade);
    }

    @Transactional
    public void removerHabilidade(Integer idFreelancer, Integer idHabilidade) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        Habilidade habilidade = habilidadeRepository.findById(idHabilidade)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada: " + idHabilidade));
        
        freelancer.removerHabilidade(habilidade);
    }

    public List<Habilidade> listarHabilidades(Integer idFreelancer) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        return freelancer.getHabilidades().stream()
                .map(FreelancerHabilidade::getHabilidade)
                .toList();
    }

    public List<Freelancer> buscarPorHabilidade(String nomeHabilidade) {
        return freelancerRepository.findByHabilidadeNome(nomeHabilidade);
    }

    public List<Freelancer> buscarPorHabilidadeId(Integer idHabilidade) {
        return freelancerRepository.findByHabilidadeId(idHabilidade);
    }

    // ========== MÉTODOS DE IDIOMAS ==========

    @Transactional
    public void adicionarIdioma(Integer idFreelancer, Integer idIdioma, LanguageLevel nivelProficiencia) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        Idioma idioma = idiomaRepository.findById(idIdioma)
                .orElseThrow(() -> new RuntimeException("Idioma não encontrado: " + idIdioma));
        
        freelancer.adicionarIdioma(idioma, nivelProficiencia);
    }

    @Transactional
    public void atualizarNivelIdioma(Integer idFreelancer, Integer idIdioma, LanguageLevel novoNivel) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        Idioma idioma = idiomaRepository.findById(idIdioma)
                .orElseThrow(() -> new RuntimeException("Idioma não encontrado: " + idIdioma));
        
        freelancer.atualizarNivelIdioma(idioma, novoNivel);
    }

    @Transactional
    public void removerIdioma(Integer idFreelancer, Integer idIdioma) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        Idioma idioma = idiomaRepository.findById(idIdioma)
                .orElseThrow(() -> new RuntimeException("Idioma não encontrado: " + idIdioma));
        
        freelancer.removerIdioma(idioma);
    }

    public List<Idioma> listarIdiomas(Integer idFreelancer) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        return freelancer.getIdiomas().stream()
                .map(FreelancerIdioma::getIdioma)
                .toList();
    }

    public List<Freelancer> buscarPorIdioma(String nomeIdioma) {
        return freelancerRepository.findByIdiomaNome(nomeIdioma);
    }

    public List<Freelancer> buscarPorIdiomaId(Integer idIdioma) {
        return freelancerRepository.findByIdiomaId(idIdioma);
    }

    public List<Freelancer> buscarPorNivelIdioma(LanguageLevel nivel) {
        return freelancerRepository.findByNivelIdioma(nivel.toString());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Freelancer buscarPorIdOuFalhar(Integer idFreelancer) {
        return freelancerRepository.findById(idFreelancer)
                .orElseThrow(() -> new RuntimeException("Freelancer não encontrado: " + idFreelancer));
    }

    public boolean possuiHabilidade(Integer idFreelancer, Integer idHabilidade) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        Habilidade habilidade = habilidadeRepository.findById(idHabilidade)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada: " + idHabilidade));
        
        return freelancer.possuiHabilidade(habilidade);
    }

    public boolean possuiIdioma(Integer idFreelancer, Integer idIdioma) {
        Freelancer freelancer = buscarPorIdOuFalhar(idFreelancer);
        Idioma idioma = idiomaRepository.findById(idIdioma)
                .orElseThrow(() -> new RuntimeException("Idioma não encontrado: " + idIdioma));
        
        return freelancer.possuiIdioma(idioma);
    }

    public Integer contarPorStatus(ProfileStatus status) {
        return freelancerRepository.countByStatusPerfil(status);
    }
}