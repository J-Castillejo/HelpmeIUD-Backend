package co.edu.iudigital.app.exceptions;

// Exception NotFound en el Rest
public class NotFoundException extends RestException{

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(ErrorDto errorDto) {
        super(errorDto);
    }
}
