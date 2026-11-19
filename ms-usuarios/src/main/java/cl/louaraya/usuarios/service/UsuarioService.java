package cl.louaraya.usuarios.service;

import cl.louaraya.usuarios.dto.ActualizarPerfilRequest;
import cl.louaraya.usuarios.dto.LoginRequest;
import cl.louaraya.usuarios.dto.RegistroRequest;
import cl.louaraya.usuarios.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse registrar(RegistroRequest request);
    UsuarioResponse login(LoginRequest request);
    List<UsuarioResponse> listar();
    UsuarioResponse obtener(Long id);
    UsuarioResponse actualizarPerfil(Long id, ActualizarPerfilRequest request);
    void eliminar(Long id);
}
