package br.com.securityoficial.repository;

import br.com.securityoficial.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Cacheable(value = "users", key = "#username")
    Optional<User> findByUsername(String username);

    @Cacheable(value = "users", key = "#email")
    Optional<User> findByEmail(String email);

    @Cacheable(value = "users", key = "#username")
    @Query("SELECT uz FROM User uz WHERE uz.username = :username OR uz.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username,
                                         @Param("email") String email);

    // Sempre que salvar ou deletar, limpa o cache para evitar "stale data"
    @Override
    @CacheEvict(value = "users", allEntries = true)
    <S extends User> S save(S entity);

    @Override
    @CacheEvict(value = "users", allEntries = true)
    void delete(User entity);
}