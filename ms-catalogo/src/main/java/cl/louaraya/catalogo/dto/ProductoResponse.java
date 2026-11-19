package cl.louaraya.catalogo.dto;

import cl.louaraya.catalogo.model.Producto;

public record ProductoResponse(Long id, String name, String description, String dimensions, String materials,
                               String category, Integer price, Boolean available, String imageUri, String collection) {
    public static ProductoResponse from(Producto p) {
        return new ProductoResponse(p.getId(), p.getName(), p.getDescription(), p.getDimensions(), p.getMaterials(),
                p.getCategory(), p.getPrice(), p.getAvailable(), p.getImageUri(), p.getCollection());
    }
}
