package cl.louaraya.catalogo.service;

import cl.louaraya.catalogo.dto.ProductoRequest;
import cl.louaraya.catalogo.dto.ProductoResponse;
import cl.louaraya.catalogo.exception.RecursoNoEncontradoException;
import cl.louaraya.catalogo.model.Producto;
import cl.louaraya.catalogo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(boolean soloDisponibles) {
        List<Producto> productos = soloDisponibles
                ? repository.findByAvailableTrueOrderByNameAsc()
                : repository.findAllByOrderByNameAsc();
        return productos.stream().map(ProductoResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        return ProductoResponse.from(buscar(id));
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto();
        aplicar(producto, request);
        return ProductoResponse.from(repository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscar(id);
        aplicar(producto, request);
        return ProductoResponse.from(repository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse cambiarDisponibilidad(Long id, boolean disponible) {
        Producto producto = buscar(id);
        producto.setAvailable(disponible);
        return ProductoResponse.from(repository.save(producto));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Producto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto " + id + " no encontrado"));
    }

    private void aplicar(Producto p, ProductoRequest r) {
        p.setName(r.name().trim());
        p.setDescription(texto(r.description()));
        p.setDimensions(texto(r.dimensions()));
        p.setMaterials(texto(r.materials()));
        p.setCategory(r.category().trim());
        p.setPrice(r.price());
        p.setAvailable(r.available() == null || r.available());
        p.setImageUri(r.imageUri());
        p.setCollection(r.collection());
    }

    private static String texto(String value) {
        return value == null ? "" : value.trim();
    }
}
