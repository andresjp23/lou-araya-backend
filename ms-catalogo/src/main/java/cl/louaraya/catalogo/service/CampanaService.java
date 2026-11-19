package cl.louaraya.catalogo.service;

import cl.louaraya.catalogo.dto.CampanaRequest;
import cl.louaraya.catalogo.dto.CampanaResponse;

import java.util.List;

public interface CampanaService {
    List<CampanaResponse> listar(boolean soloActivas);
    CampanaResponse obtener(Long id);
    CampanaResponse crear(CampanaRequest request);
    CampanaResponse actualizar(Long id, CampanaRequest request);
    CampanaResponse cambiarEstado(Long id, boolean activa);
    void eliminar(Long id);
}
