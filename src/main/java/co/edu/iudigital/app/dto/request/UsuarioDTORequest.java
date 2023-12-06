package co.edu.iudigital.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioDTORequest {

    @NotNull(message = "Nombre obligatorio")
    @NotBlank(message = "Nombre obligatorio")
    @Size(min = 2, max = 120)
    String nombre;

    String apellido;

    @NotNull(message = "Username obligatorio")
    @Email(message = "Formato email invalido")
    @JsonProperty("user_name")
    String userName;

    @Size(min = 7, message = "Debe sumistrar una contraseña segura")
    String password;

    //SSO: Single Sign On
    @JsonProperty("red_social")
    Boolean redSocial;

    @JsonProperty("fecha_nacimiento")
    LocalDate fechaNacimiento;

    String imagen;

    boolean disponibilidad;
}
