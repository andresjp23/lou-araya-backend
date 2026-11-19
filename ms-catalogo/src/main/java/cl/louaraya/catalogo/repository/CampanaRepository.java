package cl.louaraya.catalogo.repository;

import cl.louaraya.catalogo.model.Campana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampanaRepository extends JpaRepository<Campana, Long> {
    List<Campana> findAllByOrderByStartDateDesc();
    List<Campana> findByActiveTrueOrderByStartDateDesc();
}
