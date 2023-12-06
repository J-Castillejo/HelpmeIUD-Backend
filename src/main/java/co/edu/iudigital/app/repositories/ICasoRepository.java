package co.edu.iudigital.app.repositories;

import co.edu.iudigital.app.models.Caso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICasoRepository extends JpaRepository <Caso, Long> {

    //UPDATE Nombre_Table SET is_visible=false WHERE id=2; actualiza a no visible el caso 2
    @Query("UPDATE Caso c SET c.isVisible=?1 WHERE id=?2")
    Boolean setVisible(Boolean visible, Long id);
}
