package cl.louaraya.catalogo.service;

import cl.louaraya.catalogo.dto.ProductoRequest;
import cl.louaraya.catalogo.dto.ProductoResponse;

import java.util.List;

public interface ProductoService {
    List<ProductoResponse> listar(boolean soloDisponibles);
    ProductoResponse obtener(Long id);
    ProductoResponse crear(ProductoRequest request);
    ProductoResponse actualizar(Long id, ProductoRequest request);
    ProductoResponse cambiarDisponibilidad(Long id, boolean disponible);
    void eliminar(Long id);
}
