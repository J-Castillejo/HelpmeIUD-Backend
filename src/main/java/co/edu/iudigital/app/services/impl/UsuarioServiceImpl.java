package co.edu.iudigital.app.services.impl;

import co.edu.iudigital.app.dto.request.UsuarioDTORequest;
import co.edu.iudigital.app.dto.response.UsuarioDTO;
import co.edu.iudigital.app.exceptions.BadRequestException;
import co.edu.iudigital.app.exceptions.ErrorDto;
import co.edu.iudigital.app.exceptions.InternalServerErrorException;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.models.Role;
import co.edu.iudigital.app.models.Usuario;
import co.edu.iudigital.app.repositories.IUsuarioRepository;
import co.edu.iudigital.app.services.iface.IUsuarioService;
import co.edu.iudigital.app.util.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {

    @Value("emailserver.enabled")
    private Boolean emailEnabled;
    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository; //inyeccion de dependencia por atributo

    @Autowired
    private EmailService emailService;

    @Override
    public List<UsuarioDTO> consultarTodos() {
        return null;
    }

    @Override
    public UsuarioDTO consultarPorId(Long id) {
        return null;
    }


    @Override
    public UsuarioDTO consultarPorUserName(String userName) {
        return null;
    }

    @Override
    public UsuarioDTO guardar(UsuarioDTORequest usuarioDTORequest) throws RestException{

        Usuario usuario;
        Role role = new Role();
        role.setId(2L);

        usuario = usuarioRepository.findByUserName(usuarioDTORequest.getUserName());
        if(usuario != null){
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Este usuario ya existe")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        usuario = new Usuario();
        // Convertir UsuarioDTORequest a Usuario
        log.info("password cifrada{}", passwordEncoder.encode(usuarioDTORequest.getPassword()));
        usuario.setNombre(usuarioDTORequest.getNombre());
        usuario.setApellido(usuarioDTORequest.getApellido());
        usuario.setUserName(usuarioDTORequest.getUserName());
        usuario.setPassword(passwordEncoder.encode(usuarioDTORequest.getPassword()));
        usuario.setFechaNacimiento(usuarioDTORequest.getFechaNacimiento());
        usuario.setImagen(usuarioDTORequest.getImagen());
        usuario.setDisponibilidad(true);
        usuario.setRedSocial(false);
        usuario.setRoles(Collections.singletonList(role));

        usuario =  usuarioRepository.save(usuario);

        // Convertir Usuario a UsuarioDTORequest
        if(usuario!= null && usuario.getUserName() != null) {
            if(Boolean.TRUE.equals(emailEnabled)) {
                String mensaje = "Su usuario: "+usuario.getUserName()+"; password: "+usuarioDTORequest.getPassword();
                String asunto = "Registro en HelmeIUD";
                emailService.sendEmail(
                        mensaje,
                        usuario.getUserName(),
                        asunto
                );
            }
        }

        return UsuarioDTO.builder()
                .userName(usuario.getUserName())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .disponibilidad(usuario.isDisponibilidad())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .redSocial(usuario.getRedSocial())
                .imagen(usuario.getImagen())
                .roles(usuario.getRoles()
                        .stream().map(r -> r.getNombre())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByUserName(String username) {
        return usuarioRepository.findByUserName(username);
    }

    @Override
    public UsuarioDTO userInfo(Authentication authentication) throws RestException {
        if(!authentication.isAuthenticated()) {
            throw new RestException(
                    ErrorDto.builder()
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_NOT_AUTHORIZED)
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        String userName = authentication.getName();
        Usuario usuario = usuarioRepository.findByUserName(userName);
        if(usuario==null) {
            throw new RestException(
                    ErrorDto.builder()
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_NOT_AUTHORIZED)
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .imagen(usuario.getImagen())
                .roles(usuario.getRoles()
                        .stream().map(r -> r.getNombre())
                        .collect(Collectors.toList()))
                .userName(usuario.getUserName())
                .build();
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws RestException {
        if(usuario == null) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_ERROR_DATA)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
        if(usuario.getId() == null) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_ERROR_DATA)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
        Boolean exists = usuarioRepository.existsById(usuario.getId());
        if(!exists) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_ERROR_DATA)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)// Por ser una consulta, readOnly
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(userName);
        if(usuario == null) {
            log.error("Error de login, no existe usuario: "+ usuario);
            throw new UsernameNotFoundException("Error de login, no existe usuario: "+ userName);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role: usuario.getRoles()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getNombre());
            log.info("Rol {}", authority.getAuthority());
            authorities.add(authority);
        }
        return new User(usuario.getUserName(), usuario.getPassword(), usuario.isDisponibilidad(), true, true, true,authorities);
    }
}
