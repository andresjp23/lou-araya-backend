package cl.louaraya.catalogo.config;

import cl.louaraya.catalogo.model.Campana;
import cl.louaraya.catalogo.model.Producto;
import cl.louaraya.catalogo.repository.CampanaRepository;
import cl.louaraya.catalogo.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/** Carga el catálogo inicial de Lou Araya la primera vez que se inicia el servicio. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductoRepository productos;
    private final CampanaRepository campanas;

    public DataSeeder(ProductoRepository productos, CampanaRepository campanas) {
        this.productos = productos;
        this.campanas = campanas;
    }

    @Override
    public void run(String... args) {
        if (productos.count() == 0) {
            producto("Arco de ceremonia", "Estructura artesanal para ceremonias y celebraciones.", "220 x 180 cm", "Algodón natural", "Matrimonios", 120000, "Bodas de Ensueño", "drawable:arco_de_ceremonia");
            producto("Tapiz mural", "Pieza tejida a mano inspirada en la naturaleza andina.", "90 x 70 cm", "Algodón natural", "Interiores", 65000, "Hogares con Alma", "drawable:tapiz_mural");
            producto("Colgante de plantas", "Macetero colgante de fibras naturales.", "45 cm", "Yute y algodón", "Interiores", 28000, "Hogares con Alma", "drawable:macetero_colgante");
            producto("Cortina decorativa", "Cortina artesanal para ambientes cálidos.", "200 x 180 cm", "Algodón", "Festivales", 85000, "Festivales Libres", "drawable:cortina_decorativa");
            producto("Atrapasueños", "Decoración artesanal liviana.", "40 cm", "Algodón y madera", "Festivales", 32000, "Viajes que Inspiran", "drawable:atrapasuenos");
            producto("Centro de mesa", "Centro de mesa tejido para celebraciones y matrimonios.", "35 x 45 cm", "Algodón natural y flores secas", "Matrimonios", 45000, "Bodas de Ensueño", "drawable:centro_de_mesa");
            producto("Colgante textil festival", "Pieza textil decorativa para escenarios y encuentros al aire libre.", "120 x 100 cm", "Algodón y madera", "Festivales", 72000, "Festivales Libres", "drawable:colgante_textil_festival");
            producto("Tapiz mural Sol Andino", "Tapiz inspirado en el sol y los paisajes de la cordillera.", "110 x 90 cm", "Algodón natural y madera", "Interiores", 78000, "Hogares con Alma", "drawable:tapiz_mural_sol_andino");
        }
        if (campanas.count() == 0) {
            campana("Bodas de Ensueño", "Diseños para un día inolvidable", "2026-01-01", "2026-12-31", true);
            campana("Festivales Libres", "Arte textil para grandes encuentros", "2026-09-01", "2027-03-31", true);
            campana("Hogares con Alma", "Decora tus espacios", "2026-06-01", "2026-12-31", true);
            campana("Viajes que Inspiran", "Piezas livianas y únicas", "2025-01-01", "2025-12-31", false);
        }
    }

    private void producto(String name, String description, String dimensions, String materials, String category,
                          int price, String collection, String imageUri) {
        Producto p = new Producto();
        p.setName(name);
        p.setDescription(description);
        p.setDimensions(dimensions);
        p.setMaterials(materials);
        p.setCategory(category);
        p.setPrice(price);
        p.setAvailable(true);
        p.setCollection(collection);
        p.setImageUri(imageUri);
        productos.save(p);
    }

    private void campana(String title, String description, String start, String end, boolean active) {
        Campana c = new Campana();
        c.setTitle(title);
        c.setDescription(description);
        c.setStartDate(LocalDate.parse(start));
        c.setEndDate(LocalDate.parse(end));
        c.setActive(active);
        campanas.save(c);
    }
}
