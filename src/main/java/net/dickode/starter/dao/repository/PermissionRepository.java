package bf.orange.authservice.model.repository;

import bf.orange.authservice.model.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    public Permission findByName(String name);
    public Permission findByCode(String name);
}
