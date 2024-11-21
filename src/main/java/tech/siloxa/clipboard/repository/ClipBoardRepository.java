package tech.siloxa.clipboard.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.siloxa.clipboard.domain.ClipBoard;

import java.util.List;

/**
 * Spring Data JPA repository for the ClipBoard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClipBoardRepository extends JpaRepository<ClipBoard, Long> {

    List<ClipBoard> findAllByWorkSpace_id(Long workSpaceId);
}
