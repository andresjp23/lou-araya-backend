package cl.louaraya.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres") String name,
        @NotBlank(message = "El alias es obligatorio") @Size(min = 3, message = "El alias debe tener al menos 3 caracteres") String alias,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "Ingresa un correo válido") String email,
        @NotBlank(message = "El teléfono es obligatorio") @Size(min = 8, message = "Ingresa un teléfono válido") String phone,
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "Debe tener al menos 6 caracteres") String password,
        String avatarUri
) {}
