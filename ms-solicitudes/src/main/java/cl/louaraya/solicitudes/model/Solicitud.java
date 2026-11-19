package cl.louaraya.solicitudes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitudes")
public class Solicitud extends EntidadBase {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String userName = "";

    private Long productId;

    @Column(nullable = false)
    private String productName = "";

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String dimensions;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String colors;

    @Column(nullable = false)
    private String requiredDate;

    @Column(nullable = false, length = 1000)
    private String observations = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud status = EstadoSolicitud.PENDIENTE;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getColors() { return colors; }
    public void setColors(String colors) { this.colors = colors; }
    public String getRequiredDate() { return requiredDate; }
    public void setRequiredDate(String requiredDate) { this.requiredDate = requiredDate; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public EstadoSolicitud getStatus() { return status; }
    public void setStatus(EstadoSolicitud status) { this.status = status; }
}
