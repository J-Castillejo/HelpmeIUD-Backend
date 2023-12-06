package co.edu.iudigital.app.services.iface;

import co.edu.iudigital.app.dto.request.DelitoDTORequest;
import co.edu.iudigital.app.dto.response.DelitoDTO;
import co.edu.iudigital.app.exceptions.RestException;

import java.util.List;

public interface IDelitoService {

    // Consultar todos los usuarios por delitos
    List<DelitoDTO> consultarTodos();

    // Consultar delito por su ID
    DelitoDTO consultarPorId(Long id);

    // Guarda un delito
    DelitoDTO guardarDelito(DelitoDTORequest delitoDTORequest) throws RestException;

    // Borrar un delito por ID
    void borrarDelitoPorId(Long id);
}
