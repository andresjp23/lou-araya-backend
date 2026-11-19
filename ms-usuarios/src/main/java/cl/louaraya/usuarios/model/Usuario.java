package cl.louaraya.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario extends EntidadBase {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String alias;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol role = Rol.CLIENT;

    private String avatarUri;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Rol getRole() { return role; }
    public void setRole(Rol role) { this.role = role; }
    public String getAvatarUri() { return avatarUri; }
    public void setAvatarUri(String avatarUri) { this.avatarUri = avatarUri; }
}
