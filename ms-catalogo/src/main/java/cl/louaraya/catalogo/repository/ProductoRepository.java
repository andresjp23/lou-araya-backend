package cl.louaraya.catalogo.repository;

import cl.louaraya.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByOrderByNameAsc();
    List<Producto> findByAvailableTrueOrderByNameAsc();
}
