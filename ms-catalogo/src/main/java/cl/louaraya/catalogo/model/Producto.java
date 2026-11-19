package cl.louaraya.catalogo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto extends EntidadBase {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description = "";

    @Column(nullable = false)
    private String dimensions = "";

    @Column(nullable = false)
    private String materials = "";

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer price = 0;

    @Column(nullable = false)
    private Boolean available = true;

    private String imageUri;

    private String collection;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
    public String getMaterials() { return materials; }
    public void setMaterials(String materials) { this.materials = materials; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public String getCollection() { return collection; }
    public void setCollection(String collection) { this.collection = collection; }
}
