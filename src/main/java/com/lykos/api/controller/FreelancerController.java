package com.lykos.api.controller;

import com.lykos.application.service.FreelancerService;
import com.lykos.application.service.StorageService;
import com.lykos.domain.model.Freelancer;
import com.lykos.domain.model.Habilidade;
import com.lykos.domain.model.Idioma;
import com.lykos.domain.model.enums.LanguageLevel;
import com.lykos.domain.model.enums.ProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/freelancers")
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerService freelancerService;
    

    // ========== OPERAÇÕES BÁSICAS DO FREELANCER ==========

    @PostMapping("/register")
    public ResponseEntity<?> registrarFreelancer(@RequestBody RegistroFreelancerRequest request) {
        try {
            Freelancer freelancer = freelancerService.criarFreelancer(
                request.idUsuario(),
                request.nomeExibicao(),
                request.descricaoPerfil()
            );
            return ResponseEntity.status(201).body(freelancer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return freelancerService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarPorIdUsuario(@PathVariable Integer idUsuario) {
        return freelancerService.buscarPorIdUsuario(idUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Freelancer> listarTodos() {
        return freelancerService.listarTodos();
    }

    @GetMapping("/buscar/nome")
    public List<Freelancer> buscarPorNome(@RequestParam String nome) {
        return freelancerService.buscarPorNome(nome);
    }

    @GetMapping("/buscar/descricao")
    public List<Freelancer> buscarPorDescricao(@RequestParam String descricao) {
        return freelancerService.buscarPorDescricao(descricao);
    }

    @GetMapping("/buscar/status")
    public List<Freelancer> buscarPorStatus(@RequestParam ProfileStatus status) {
        return freelancerService.buscarPorStatus(status);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Integer id, @RequestBody AtualizarPerfilRequest request) {
        try {
            Freelancer freelancer = freelancerService.atualizarPerfil(
                id, request.nomeExibicao(), request.descricaoPerfil()
            );
            return ResponseEntity.ok(freelancer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Integer id, @RequestParam ProfileStatus status) {
        try {
            freelancerService.atualizarStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarFreelancer(@PathVariable Integer id) {
        try {
            freelancerService.deletarFreelancer(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ========== UPLOAD DE FOTO DE PERFIL ==========

    @PutMapping("/{id}/foto")
    public ResponseEntity<?> atualizarFotoPerfil(
            @PathVariable Integer id,
            @RequestParam("foto") MultipartFile foto) {
        try {
            String fileName = foto.getOriginalFilename();
            Freelancer freelancer = freelancerService.atualizarFotoPerfil(
                id, foto.getBytes(), fileName
            );
            return ResponseEntity.ok(freelancer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar foto: " + e.getMessage());
        }
    }

    // ========== OPERAÇÕES DE HABILIDADES ==========

    @PostMapping("/{id}/habilidades")
    public ResponseEntity<?> adicionarHabilidade(
            @PathVariable Integer id,
            @RequestParam Integer idHabilidade) {
        try {
            freelancerService.adicionarHabilidade(id, idHabilidade);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/habilidades/{idHabilidade}")
    public ResponseEntity<?> removerHabilidade(
            @PathVariable Integer id,
            @PathVariable Integer idHabilidade) {
        try {
            freelancerService.removerHabilidade(id, idHabilidade);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/habilidades")
    public ResponseEntity<?> listarHabilidades(@PathVariable Integer id) {
        try {
            List<Habilidade> habilidades = freelancerService.listarHabilidades(id);
            return ResponseEntity.ok(habilidades);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/habilidade")
    public List<Freelancer> buscarPorHabilidade(@RequestParam String habilidade) {
        return freelancerService.buscarPorHabilidade(habilidade);
    }

    @GetMapping("/buscar/habilidade/{idHabilidade}")
    public List<Freelancer> buscarPorHabilidadeId(@PathVariable Integer idHabilidade) {
        return freelancerService.buscarPorHabilidadeId(idHabilidade);
    }

    @GetMapping("/{id}/habilidades/{idHabilidade}/possui")
    public ResponseEntity<Boolean> possuiHabilidade(
            @PathVariable Integer id,
            @PathVariable Integer idHabilidade) {
        try {
            boolean possui = freelancerService.possuiHabilidade(id, idHabilidade);
            return ResponseEntity.ok(possui);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    // ========== OPERAÇÕES DE IDIOMAS ==========

    @PostMapping("/{id}/idiomas")
    public ResponseEntity<?> adicionarIdioma(
            @PathVariable Integer id,
            @RequestParam Integer idIdioma,
            @RequestParam LanguageLevel nivelProficiencia) {
        try {
            freelancerService.adicionarIdioma(id, idIdioma, nivelProficiencia);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/idiomas/{idIdioma}")
    public ResponseEntity<?> atualizarNivelIdioma(
            @PathVariable Integer id,
            @PathVariable Integer idIdioma,
            @RequestParam LanguageLevel nivel) {
        try {
            freelancerService.atualizarNivelIdioma(id, idIdioma, nivel);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/idiomas/{idIdioma}")
    public ResponseEntity<?> removerIdioma(
            @PathVariable Integer id,
            @PathVariable Integer idIdioma) {
        try {
            freelancerService.removerIdioma(id, idIdioma);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/idiomas")
    public ResponseEntity<?> listarIdiomas(@PathVariable Integer id) {
        try {
            List<Idioma> idiomas = freelancerService.listarIdiomas(id);
            return ResponseEntity.ok(idiomas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/idioma")
    public List<Freelancer> buscarPorIdioma(@RequestParam String idioma) {
        return freelancerService.buscarPorIdioma(idioma);
    }

    @GetMapping("/buscar/idioma/{idIdioma}")
    public List<Freelancer> buscarPorIdiomaId(@PathVariable Integer idIdioma) {
        return freelancerService.buscarPorIdiomaId(idIdioma);
    }

    @GetMapping("/buscar/idioma/nivel")
    public List<Freelancer> buscarPorNivelIdioma(@RequestParam LanguageLevel nivel) {
        return freelancerService.buscarPorNivelIdioma(nivel);
    }

    @GetMapping("/{id}/idiomas/{idIdioma}/possui")
    public ResponseEntity<Boolean> possuiIdioma(
            @PathVariable Integer id,
            @PathVariable Integer idIdioma) {
        try {
            boolean possui = freelancerService.possuiIdioma(id, idIdioma);
            return ResponseEntity.ok(possui);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    // ========== ESTATÍSTICAS ==========

    // @GetMapping("/estatisticas/total")
    // public Integer contarTotal() {
    //     return freelancerService.contarTotal();
    // }

    @GetMapping("/estatisticas/status")
    public Integer contarPorStatus(@RequestParam ProfileStatus status) {
        return freelancerService.contarPorStatus(status);
    }

    // ========== RECORDS PARA DTOs ==========

    public record RegistroFreelancerRequest(
        Integer idUsuario,
        String nomeExibicao,
        String descricaoPerfil
    ) {}

    public record AtualizarPerfilRequest(
        String nomeExibicao,
        String descricaoPerfil
    ) {}
}