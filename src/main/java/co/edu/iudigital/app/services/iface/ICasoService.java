package co.edu.iudigital.app.services.iface;

import co.edu.iudigital.app.dto.CasoDTO;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.models.Caso;

import java.util.List;

public interface ICasoService {

    List<CasoDTO> consultarTodos();

    Caso crear(CasoDTO casoDTO) throws RestException;

    Boolean visible(Boolean visible, Long id);

    CasoDTO consultarPorId(Long id);
}
