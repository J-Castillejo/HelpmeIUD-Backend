package co.edu.iudigital.app.services.impl;

import co.edu.iudigital.app.dto.CasoDTO;
import co.edu.iudigital.app.exceptions.BadRequestException;
import co.edu.iudigital.app.exceptions.ErrorDto;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.models.Caso;
import co.edu.iudigital.app.models.Delito;
import co.edu.iudigital.app.models.Usuario;
import co.edu.iudigital.app.repositories.ICasoRepository;
import co.edu.iudigital.app.repositories.IDelitoRepository;
import co.edu.iudigital.app.repositories.IUsuarioRepository;
import co.edu.iudigital.app.services.iface.ICasoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CasoServiceImpl implements ICasoService {

    @Autowired
    private ICasoRepository casoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IDelitoRepository delitoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CasoDTO> consultarTodos() {
        log.info("Listando todos los casos {}");
        List<Caso> casos = casoRepository.findAll();

        // Programación funcional: lambdas Java
        return casos.stream().map(caso ->
                CasoDTO.builder()
                        .id(caso.getId())
                        .descripcion(caso.getDescripcion())
                        .altitud(caso.getAltitud())
                        .latitud(caso.getLatitud())
                        .longitud(caso.getLongitud())
                        .isVisible(caso.getIsVisible())
                        .fechaHora(caso.getFechaHora())
                        .rmiUrl(caso.getRmiUrl())
                        .urlMap(caso.getUrlMap())
                        .usuarioId(caso.getUsuario().getId())
                        .delitoId(caso.getDelito().getId())
                        .build()
        ).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public Caso crear(CasoDTO casoDTO) throws RestException {

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(casoDTO.getUsuarioId());

        Optional<Delito> delitoOptional = delitoRepository.findById(casoDTO.getDelitoId());

        if(!usuarioOptional.isPresent() || !delitoOptional.isPresent()){
            log.error("No existe usuario {}", casoDTO.getUsuarioId());
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe usuario o delito")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        Caso caso = new Caso();

        caso.setFechaHora(casoDTO.getFechaHora());
        caso.setLatitud(casoDTO.getLatitud());
        caso.setLongitud(casoDTO.getLongitud());
        caso.setAltitud(casoDTO.getAltitud());
        caso.setDescripcion(casoDTO.getDescripcion());
        caso.setIsVisible(true);
        caso.setUrlMap(casoDTO.getUrlMap());
        caso.setRmiUrl(casoDTO.getRmiUrl());
        caso.setUsuario(usuarioOptional.get());
        caso.setDelito(delitoOptional.get());

        return casoRepository.save(caso);

    }

    @Transactional
    @Override
    public Boolean visible(Boolean visible, Long id) {
        return casoRepository.setVisible(visible,id);
    }

    @Transactional(readOnly = true)
    @Override
    public CasoDTO consultarPorId(Long id) {
        Optional<Caso> casoOptional = casoRepository.findById(id);

        if(casoOptional.isPresent()){
            Caso caso = casoOptional.get();
            CasoDTO.builder()
                    .id(caso.getId())
                    .descripcion(caso.getDescripcion())
                    .altitud(caso.getAltitud())
                    .latitud(caso.getLatitud())
                    .longitud(caso.getLongitud())
                    .isVisible(caso.getIsVisible())
                    .fechaHora(caso.getFechaHora())
                    .rmiUrl(caso.getRmiUrl())
                    .urlMap(caso.getUrlMap())
                    .usuarioId(caso.getUsuario().getId())
                    .delitoId(caso.getDelito().getId())
                    .build();
        }
        log.warn("No existe usuario {}", id);
        return  null;

    }
}
