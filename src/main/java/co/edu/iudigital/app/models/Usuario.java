package co.edu.iudigital.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "nombre", nullable = false, length = 120)
    String nombre;

    @Column(name = "apellido", length = 120)
    String apellido;

    @Column(name = "user_name",unique = true, length = 120,nullable = false)
    String userName;

    @Column(name = "password", nullable = false)
    String password;

    //SSO: Single Sign On
    @Column(name = "red_social")
    Boolean redSocial;

    @Column(name = "fecha_nacimiento", nullable = false)
    LocalDate fechaNacimiento;


    @Column
    String imagen;

    @Column
    boolean enabled;

    /*@OneToMany*/

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "roles_usuario",
            joinColumns = {@JoinColumn(name = "usuarios_id")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id")}
    )

    @JsonIgnore
    List<Role> roles;

    public Usuario() {
        this.enabled = true;  // Valor por defecto para 'enabled si no se agrega'
        this.redSocial = false;  // Valor por defecto para 'redSocial si no se agrega'
    }
}
