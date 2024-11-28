package tech.siloxa.clipboard.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.siloxa.clipboard.domain.Session;
import tech.siloxa.clipboard.domain.User;

/**
 * Spring Data JPA repository for the Session entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("select session from Session session where session.user.login = ?#{principal.username}")
    List<Session> findByUserIsCurrentUser();
}
