package cl.louaraya.catalogo.service;

import cl.louaraya.catalogo.dto.CampanaRequest;
import cl.louaraya.catalogo.dto.CampanaResponse;
import cl.louaraya.catalogo.exception.RecursoNoEncontradoException;
import cl.louaraya.catalogo.exception.ReglaNegocioException;
import cl.louaraya.catalogo.model.Campana;
import cl.louaraya.catalogo.repository.CampanaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampanaServiceImpl implements CampanaService {

    private final CampanaRepository repository;

    public CampanaServiceImpl(CampanaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampanaResponse> listar(boolean soloActivas) {
        List<Campana> campanas = soloActivas
                ? repository.findByActiveTrueOrderByStartDateDesc()
                : repository.findAllByOrderByStartDateDesc();
        return campanas.stream().map(CampanaResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CampanaResponse obtener(Long id) {
        return CampanaResponse.from(buscar(id));
    }

    @Override
    @Transactional
    public CampanaResponse crear(CampanaRequest request) {
        Campana campana = new Campana();
        aplicar(campana, request);
        return CampanaResponse.from(repository.save(campana));
    }

    @Override
    @Transactional
    public CampanaResponse actualizar(Long id, CampanaRequest request) {
        Campana campana = buscar(id);
        aplicar(campana, request);
        return CampanaResponse.from(repository.save(campana));
    }

    @Override
    @Transactional
    public CampanaResponse cambiarEstado(Long id, boolean activa) {
        Campana campana = buscar(id);
        campana.setActive(activa);
        return CampanaResponse.from(repository.save(campana));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Campana buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Campaña " + id + " no encontrada"));
    }

    private void aplicar(Campana c, CampanaRequest r) {
        if (r.endDate().isBefore(r.startDate())) {
            throw new ReglaNegocioException("La fecha de término debe ser posterior al inicio");
        }
        c.setTitle(r.title().trim());
        c.setDescription(r.description() == null ? "" : r.description().trim());
        c.setStartDate(r.startDate());
        c.setEndDate(r.endDate());
        c.setActive(r.active() == null || r.active());
    }
}
