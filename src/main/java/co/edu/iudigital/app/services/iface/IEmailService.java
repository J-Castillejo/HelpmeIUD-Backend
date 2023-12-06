package co.edu.iudigital.app.services.iface;

public interface IEmailService {

    boolean sendEmail(String mensaje, String email, String asunto);

}
