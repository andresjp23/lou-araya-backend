package cl.louaraya.usuarios.service;

import cl.louaraya.usuarios.dto.ActualizarPerfilRequest;
import cl.louaraya.usuarios.dto.LoginRequest;
import cl.louaraya.usuarios.dto.RegistroRequest;
import cl.louaraya.usuarios.dto.UsuarioResponse;
import cl.louaraya.usuarios.exception.ConflictoException;
import cl.louaraya.usuarios.exception.CredencialesInvalidasException;
import cl.louaraya.usuarios.exception.RecursoNoEncontradoException;
import cl.louaraya.usuarios.model.Rol;
import cl.louaraya.usuarios.model.Usuario;
import cl.louaraya.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponse registrar(RegistroRequest request) {
        String email = request.email().trim();
        String alias = request.alias().trim();
        if (repository.existsByEmailIgnoreCase(email)) throw new ConflictoException("El correo ya está registrado");
        if (repository.existsByAliasIgnoreCase(alias)) throw new ConflictoException("El alias ya está en uso");

        Usuario usuario = new Usuario();
        usuario.setName(request.name().trim());
        usuario.setAlias(alias);
        usuario.setEmail(email);
        usuario.setPhone(request.phone().trim());
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setRole(Rol.CLIENT);
        usuario.setAvatarUri(request.avatarUri());
        return UsuarioResponse.from(repository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse login(LoginRequest request) {
        Usuario usuario = repository.findByEmailIgnoreCase(request.email().trim())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));
        return UsuarioResponse.from(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return repository.findAll().stream().map(UsuarioResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return UsuarioResponse.from(buscar(id));
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarPerfil(Long id, ActualizarPerfilRequest request) {
        Usuario usuario = buscar(id);
        String alias = request.alias().trim();
        if (repository.existsByAliasIgnoreCaseAndIdNot(alias, id)) throw new ConflictoException("El alias ya está en uso");
        usuario.setName(request.name().trim());
        usuario.setAlias(alias);
        usuario.setPhone(request.phone().trim());
        if (request.avatarUri() != null) usuario.setAvatarUri(request.avatarUri());
        return UsuarioResponse.from(repository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario " + id + " no encontrado"));
    }
}
