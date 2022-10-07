package bf.orange.authservice.model.repository;

import bf.orange.authservice.model.entities.User;
import brave.internal.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByMsisdn(String msisdn);
}
