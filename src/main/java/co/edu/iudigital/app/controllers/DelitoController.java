package co.edu.iudigital.app.controllers;

import co.edu.iudigital.app.dto.request.DelitoDTORequest;
import co.edu.iudigital.app.dto.response.DelitoDTO;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.services.iface.IDelitoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/delitos")
@Api(value = "/delitos", tags = {"Delitos"})
@SwaggerDefinition(tags = {
        @Tag(name = "Delitos", description = "Gestion API Delitos")
})
public class DelitoController {

    @Autowired
    IDelitoService delitoService;

    @ApiOperation(value = "Obtiene todos delitos",
            responseContainer = "List",
            produces = "application/json",
            httpMethod = "GET")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<DelitoDTO>> index(){
        return ResponseEntity
                .ok()
                .body(
                        delitoService.consultarTodos()
                );

    }

    @ApiOperation(value = "Guardar un Delito",
            response = DelitoDTO.class,
            responseContainer = "DelitoDTO",
            produces = "application/json",
            httpMethod = "POST")
    @Secured("ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DelitoDTO> create(@Valid @RequestBody DelitoDTORequest delitoDTORequest)
            throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        delitoService.guardarDelito(delitoDTORequest)
                );
    }
}
