package com.lykos.domain.repository;

import com.lykos.domain.model.Usuario;
import com.lykos.domain.model.enums.UserType;
import com.lykos.domain.model.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findById(Integer id);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);
    boolean existsByNomeUsuario(String nomeUsuario);

    List<Usuario> findByTipoUsuario(UserType tipoUsuario);
    List<Usuario> findByStatusConta(AccountStatus statusConta);
    List<Usuario> findByTipoUsuarioAndStatusConta(UserType tipoUsuario, AccountStatus statusConta);

    List<Usuario> findByNomeCompletoContainingIgnoreCase(String nome);
    Optional<Usuario> findByTelefone(String telefone);

    List<Usuario> findByDataCadastroAfter(LocalDateTime data);
    List<Usuario> findByDataCadastroBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Usuario> findByStatusContaAndUltimoLoginBefore(
            AccountStatus status, LocalDateTime dataLimite);

    // Novo: buscar por múltiplos status
    List<Usuario> findByStatusContaIn(List<AccountStatus> statuses);

    // Novo: buscar usuários que nunca fizeram login
    List<Usuario> findByUltimoLoginIsNull();

    // Novo: contar por status
    Integer countByStatusConta(AccountStatus status);

    @Query("SELECT u FROM Usuario u WHERE u.email LIKE %:dominio")
    List<Usuario> findByEmailDomain(@Param("dominio") String dominio);

    // Buscar por parte do nome de usuário
    List<Usuario> findByNomeUsuarioContainingIgnoreCase(String nomeUsuario);

    // Para verificar email excluindo o próprio usuário
    boolean existsByEmailAndIdUsuarioNot(String email, Integer idUsuario);

    // Para verificar nome de usuário excluindo o próprio usuário
    boolean existsByNomeUsuarioAndIdUsuarioNot(String nomeUsuario, Integer idUsuario);
}