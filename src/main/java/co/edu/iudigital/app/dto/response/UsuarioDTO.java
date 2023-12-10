package co.edu.iudigital.app.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioDTO {

    Long id;

    String nombre;

    String apellido;

    String userName;

    Boolean redSocial;

    LocalDate fechaNacimiento;

    String imagen;

    boolean enabled;

    List<String> roles;

}
