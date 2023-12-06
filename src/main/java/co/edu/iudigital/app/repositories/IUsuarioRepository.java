package co.edu.iudigital.app.repositories;

import co.edu.iudigital.app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository <Usuario, Long> {

    Usuario findByUserName(String userName); // Spring Data JPA

}
