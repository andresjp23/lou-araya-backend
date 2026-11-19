package cl.louaraya.usuarios.dto;

import cl.louaraya.usuarios.model.Usuario;

/** Respuesta pública del usuario: nunca expone la contraseña. */
public record UsuarioResponse(Long id, String name, String alias, String email, String phone, String role, String avatarUri) {
    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getName(), u.getAlias(), u.getEmail(), u.getPhone(), u.getRole().name(), u.getAvatarUri());
    }
}
