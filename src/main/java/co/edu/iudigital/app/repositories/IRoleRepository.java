package co.edu.iudigital.app.repositories;

import co.edu.iudigital.app.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository
        extends CrudRepository<Role, Long> {
}
