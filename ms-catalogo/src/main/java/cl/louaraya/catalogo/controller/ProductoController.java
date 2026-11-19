package cl.louaraya.catalogo.controller;

import cl.louaraya.catalogo.dto.DisponibilidadRequest;
import cl.louaraya.catalogo.dto.ProductoRequest;
import cl.louaraya.catalogo.dto.ProductoResponse;
import cl.louaraya.catalogo.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(defaultValue = "false") boolean disponibles) {
        return service.listar(disponibles);
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/disponibilidad")
    public ProductoResponse disponibilidad(@PathVariable Long id, @Valid @RequestBody DisponibilidadRequest request) {
        return service.cambiarDisponibilidad(id, request.value());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
