package co.edu.iudigital.app.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String error;

    private String message;

    private int status;

    private LocalDateTime date;
}
