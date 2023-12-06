package co.edu.iudigital.app.services.iface;

import co.edu.iudigital.app.dto.request.UsuarioDTORequest;
import co.edu.iudigital.app.dto.response.UsuarioDTO;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.models.Usuario;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUsuarioService {

    // Consultar todos los usuarios
    List<UsuarioDTO> consultarTodos();

    // Consultar un usuario por ID
    UsuarioDTO consultarPorId(Long id);

    // Consultar usuario por su username o email
    UsuarioDTO consultarPorUserName(String userName);

    UsuarioDTO guardar(UsuarioDTORequest usuarioDTORequest) throws RestException;

    Usuario findByUserName(String username);

    UsuarioDTO userInfo(Authentication authentication) throws RestException;

    Usuario actualizar(Usuario usuario) throws RestException;
}
