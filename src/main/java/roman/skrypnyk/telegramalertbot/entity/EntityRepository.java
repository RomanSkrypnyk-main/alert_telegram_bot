package roman.skrypnyk.telegramalertbot.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EntityRepository extends JpaRepository<Entity, Integer> {
    Optional<Entity> findByUserId(Long userId);

    @Query(value = "SELECT user_id FROM users", nativeQuery = true)
    List<Long> findAllUserIds();
}
