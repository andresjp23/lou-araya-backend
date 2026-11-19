package cl.louaraya.usuarios.repository;

import cl.louaraya.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByAliasIgnoreCase(String alias);
    boolean existsByAliasIgnoreCaseAndIdNot(String alias, Long id);
}
