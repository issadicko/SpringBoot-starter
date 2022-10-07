package bf.orange.authservice.model.repository;

import bf.orange.authservice.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findByName(String name);
    public Role findByCode(String name);
}
