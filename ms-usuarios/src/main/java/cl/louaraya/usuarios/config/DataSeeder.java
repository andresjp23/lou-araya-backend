package cl.louaraya.usuarios.config;

import cl.louaraya.usuarios.model.Rol;
import cl.louaraya.usuarios.model.Usuario;
import cl.louaraya.usuarios.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Crea las cuentas iniciales (administrador y cliente de prueba) si no existen. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearSiNoExiste("Administrador", "admin", "admin@louaraya.cl", "+56900000000", "Admin123", Rol.ADMIN);
        crearSiNoExiste("Cliente Demo", "cliente", "cliente@louaraya.cl", "+56911111111", "Cliente123", Rol.CLIENT);
    }

    private void crearSiNoExiste(String name, String alias, String email, String phone, String password, Rol rol) {
        if (repository.existsByEmailIgnoreCase(email)) return;
        Usuario u = new Usuario();
        u.setName(name);
        u.setAlias(alias);
        u.setEmail(email);
        u.setPhone(phone);
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setRole(rol);
        repository.save(u);
    }
}
