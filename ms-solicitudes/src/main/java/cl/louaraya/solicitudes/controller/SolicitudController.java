package cl.louaraya.solicitudes.controller;

import cl.louaraya.solicitudes.dto.EstadoRequest;
import cl.louaraya.solicitudes.dto.SolicitudRequest;
import cl.louaraya.solicitudes.dto.SolicitudResponse;
import cl.louaraya.solicitudes.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    @GetMapping
    public List<SolicitudResponse> listar(@RequestParam(required = false) Long userId,
                                          @RequestParam(required = false) String status) {
        return service.listar(userId, status);
    }

    @GetMapping("/{id}")
    public SolicitudResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudResponse crear(@Valid @RequestBody SolicitudRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public SolicitudResponse actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    public SolicitudResponse estado(@PathVariable Long id, @Valid @RequestBody EstadoRequest request) {
        return service.cambiarEstado(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
