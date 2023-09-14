package springBoot.core.b_backEnd.e_dao;

import org.springframework.data.jpa.repository.JpaRepository;
import springBoot.core.b_backEnd.c_model.User;

import java.util.Optional;

public interface UserDaoRepository extends JpaRepository<User,Long> {
        Optional<User> findByUsername(String username);
}
