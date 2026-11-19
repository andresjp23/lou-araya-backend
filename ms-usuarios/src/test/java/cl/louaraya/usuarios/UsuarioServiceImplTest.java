package cl.louaraya.usuarios;

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
import cl.louaraya.usuarios.service.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    private UsuarioRepository repository;
    private BCryptPasswordEncoder encoder;
    private UsuarioServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(UsuarioRepository.class);
        encoder = new BCryptPasswordEncoder(4);
        service = new UsuarioServiceImpl(repository, encoder);
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            if (u.getId() == null) u.setId(10L);
            return u;
        });
    }

    private Usuario usuario(Long id, String email, String password) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setName("Lou Araya");
        u.setAlias("lou");
        u.setEmail(email);
        u.setPhone("+56912345678");
        u.setPasswordHash(encoder.encode(password));
        u.setRole(Rol.CLIENT);
        return u;
    }

    @Test
    void registrarGuardaHashYRolCliente() {
        UsuarioResponse r = service.registrar(new RegistroRequest(" Lou ", "lou", "lou@mail.cl", "+56912345678", "secreta1", null));
        assertThat(r.id()).isEqualTo(10L);
        assertThat(r.name()).isEqualTo("Lou");
        assertThat(r.role()).isEqualTo("CLIENT");
        verify(repository).save(argThat(u -> !u.getPasswordHash().equals("secreta1") && encoder.matches("secreta1", u.getPasswordHash())));
    }

    @Test
    void registrarRechazaCorreoDuplicado() {
        when(repository.existsByEmailIgnoreCase("lou@mail.cl")).thenReturn(true);
        assertThatThrownBy(() -> service.registrar(new RegistroRequest("Lou", "lou", "lou@mail.cl", "+56912345678", "secreta1", null)))
                .isInstanceOf(ConflictoException.class).hasMessageContaining("correo");
    }

    @Test
    void registrarRechazaAliasDuplicado() {
        when(repository.existsByAliasIgnoreCase("lou")).thenReturn(true);
        assertThatThrownBy(() -> service.registrar(new RegistroRequest("Lou", "lou", "lou@mail.cl", "+56912345678", "secreta1", null)))
                .isInstanceOf(ConflictoException.class).hasMessageContaining("alias");
    }

    @Test
    void loginCorrectoDevuelveUsuario() {
        when(repository.findByEmailIgnoreCase("lou@mail.cl")).thenReturn(Optional.of(usuario(1L, "lou@mail.cl", "secreta1")));
        assertThat(service.login(new LoginRequest("lou@mail.cl", "secreta1")).id()).isEqualTo(1L);
    }

    @Test
    void loginConClaveIncorrectaFalla() {
        when(repository.findByEmailIgnoreCase("lou@mail.cl")).thenReturn(Optional.of(usuario(1L, "lou@mail.cl", "secreta1")));
        assertThatThrownBy(() -> service.login(new LoginRequest("lou@mail.cl", "otra")))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void actualizarPerfilCambiaDatos() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario(1L, "lou@mail.cl", "secreta1")));
        UsuarioResponse r = service.actualizarPerfil(1L, new ActualizarPerfilRequest("Lou Nueva", "lounueva", "+56999999999", null));
        assertThat(r.name()).isEqualTo("Lou Nueva");
        assertThat(r.alias()).isEqualTo("lounueva");
    }

    @Test
    void actualizarPerfilRechazaAliasDeOtro() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario(1L, "lou@mail.cl", "secreta1")));
        when(repository.existsByAliasIgnoreCaseAndIdNot("tomado", 1L)).thenReturn(true);
        assertThatThrownBy(() -> service.actualizarPerfil(1L, new ActualizarPerfilRequest("Lou", "tomado", "+56999999999", null)))
                .isInstanceOf(ConflictoException.class);
    }

    @Test
    void obtenerInexistenteLanza404() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.obtener(99L)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void listarYEliminar() {
        Usuario u = usuario(1L, "lou@mail.cl", "secreta1");
        when(repository.findAll()).thenReturn(List.of(u));
        when(repository.findById(1L)).thenReturn(Optional.of(u));
        assertThat(service.listar()).hasSize(1);
        service.eliminar(1L);
        verify(repository).delete(u);
    }
}
