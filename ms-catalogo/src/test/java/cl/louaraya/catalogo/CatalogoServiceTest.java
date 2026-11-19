package cl.louaraya.catalogo;

import cl.louaraya.catalogo.dto.CampanaRequest;
import cl.louaraya.catalogo.dto.ProductoRequest;
import cl.louaraya.catalogo.exception.RecursoNoEncontradoException;
import cl.louaraya.catalogo.exception.ReglaNegocioException;
import cl.louaraya.catalogo.model.Campana;
import cl.louaraya.catalogo.model.Producto;
import cl.louaraya.catalogo.repository.CampanaRepository;
import cl.louaraya.catalogo.repository.ProductoRepository;
import cl.louaraya.catalogo.service.CampanaServiceImpl;
import cl.louaraya.catalogo.service.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CatalogoServiceTest {

    private ProductoRepository productos;
    private CampanaRepository campanas;
    private ProductoServiceImpl productoService;
    private CampanaServiceImpl campanaService;

    @BeforeEach
    void setUp() {
        productos = mock(ProductoRepository.class);
        campanas = mock(CampanaRepository.class);
        productoService = new ProductoServiceImpl(productos);
        campanaService = new CampanaServiceImpl(campanas);
        when(productos.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));
        when(campanas.save(any(Campana.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearProductoNormalizaCamposOpcionales() {
        var r = productoService.crear(new ProductoRequest(" Tapiz ", null, null, null, "Interiores", 1000, null, null, null));
        assertThat(r.name()).isEqualTo("Tapiz");
        assertThat(r.description()).isEmpty();
        assertThat(r.available()).isTrue();
    }

    @Test
    void listarProductosFiltraDisponibles() {
        when(productos.findByAvailableTrueOrderByNameAsc()).thenReturn(List.of(new Producto()));
        when(productos.findAllByOrderByNameAsc()).thenReturn(List.of(new Producto(), new Producto()));
        assertThat(productoService.listar(true)).hasSize(1);
        assertThat(productoService.listar(false)).hasSize(2);
    }

    @Test
    void cambiarDisponibilidadYEliminarProducto() {
        Producto p = new Producto();
        p.setName("Tapiz");
        p.setAvailable(true);
        when(productos.findById(1L)).thenReturn(Optional.of(p));
        assertThat(productoService.cambiarDisponibilidad(1L, false).available()).isFalse();
        productoService.eliminar(1L);
        verify(productos).delete(p);
    }

    @Test
    void productoInexistente() {
        when(productos.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productoService.obtener(5L)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void campanaConFechasInvertidasSeRechaza() {
        var request = new CampanaRequest("Verano", "", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 4, 1), true);
        assertThatThrownBy(() -> campanaService.crear(request)).isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void crearYActualizarCampana() {
        var creada = campanaService.crear(new CampanaRequest("Verano", null, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1), null));
        assertThat(creada.active()).isTrue();
        Campana existente = new Campana();
        existente.setTitle("Verano");
        when(campanas.findById(2L)).thenReturn(Optional.of(existente));
        var actualizada = campanaService.actualizar(2L, new CampanaRequest("Invierno", "Frío", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 7, 1), false));
        assertThat(actualizada.title()).isEqualTo("Invierno");
        assertThat(actualizada.active()).isFalse();
        assertThat(campanaService.cambiarEstado(2L, true).active()).isTrue();
    }

    @Test
    void listarCampanasActivas() {
        when(campanas.findByActiveTrueOrderByStartDateDesc()).thenReturn(List.of(new Campana()));
        assertThat(campanaService.listar(true)).hasSize(1);
        verify(campanas, never()).findAllByOrderByStartDateDesc();
    }
}
