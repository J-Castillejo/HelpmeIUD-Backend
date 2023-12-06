package co.edu.iudigital.app.repositories;

import co.edu.iudigital.app.models.Delito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDelitoRepository extends JpaRepository <Delito, Long> {

    List<Delito> findByNombre(String nombre);

}
