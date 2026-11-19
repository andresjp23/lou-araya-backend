package cl.louaraya.catalogo.controller;

import cl.louaraya.catalogo.dto.CampanaRequest;
import cl.louaraya.catalogo.dto.CampanaResponse;
import cl.louaraya.catalogo.dto.DisponibilidadRequest;
import cl.louaraya.catalogo.service.CampanaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campanas")
public class CampanaController {

    private final CampanaService service;

    public CampanaController(CampanaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CampanaResponse> listar(@RequestParam(defaultValue = "false") boolean activas) {
        return service.listar(activas);
    }

    @GetMapping("/{id}")
    public CampanaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampanaResponse crear(@Valid @RequestBody CampanaRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public CampanaResponse actualizar(@PathVariable Long id, @Valid @RequestBody CampanaRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    public CampanaResponse estado(@PathVariable Long id, @Valid @RequestBody DisponibilidadRequest request) {
        return service.cambiarEstado(id, request.value());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
