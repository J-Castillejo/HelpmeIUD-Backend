package co.edu.iudigital.app.services.impl;

import co.edu.iudigital.app.dto.request.DelitoDTORequest;
import co.edu.iudigital.app.dto.response.DelitoDTO;
import co.edu.iudigital.app.exceptions.BadRequestException;
import co.edu.iudigital.app.exceptions.ErrorDto;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.models.Delito;
import co.edu.iudigital.app.models.Usuario;
import co.edu.iudigital.app.repositories.IDelitoRepository;
import co.edu.iudigital.app.repositories.IUsuarioRepository;
import co.edu.iudigital.app.services.iface.IDelitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DelitoServiceImpl implements IDelitoService {

    private IDelitoRepository delitoRepository;
    private IUsuarioRepository usuarioRepository;

    @Autowired
    public DelitoServiceImpl(IDelitoRepository delitoRepository,IUsuarioRepository usuarioRepository ){
        this.delitoRepository = delitoRepository; //inyeccion de dependencia por constructor
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DelitoDTO> consultarTodos() {

        List<Delito> delitos = delitoRepository.findAll();

        return delitos.stream().
                map(delito ->
                        DelitoDTO.builder()
                                .id(delito.getId())
                                .nombre(delito.getNombre())
                                .descripcion(delito.getDescripcion())
                                .build()
                ).collect(Collectors.toList());


    }

    @Override
    public DelitoDTO consultarPorId(Long id) {
        return null;
    }

    @Transactional
    @Override
    public DelitoDTO guardarDelito(DelitoDTORequest delitoDTORequest) throws RestException {

        // Compartir DelitoDTORequest a Delito
        Delito delito = new Delito();
        delito.setNombre(delitoDTORequest.getNombre());
        delito.setDescripcion(delitoDTORequest.getDescripcion());

        // Capturar usuario por el id
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(delitoDTORequest.getUsuarioId());

        if(!usuarioOptional.isPresent()){
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe usuario")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        delito.setUsuario(usuarioOptional.get());
        delitoRepository.save(delito);

        return DelitoDTO.builder()
                .id(delito.getId())
                .nombre(delito.getNombre())
                .descripcion(delito.getDescripcion())
                .build();
    }

    @Override
    public void borrarDelitoPorId(Long id) {
    }
}
