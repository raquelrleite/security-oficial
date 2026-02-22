package br.com.securityoficial.repository;

import br.com.securityoficial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT uz FROM User uz WHERE uz.username = :username OR uz.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username,
                                         @Param("email") String email);

    void delete(User entity);
}