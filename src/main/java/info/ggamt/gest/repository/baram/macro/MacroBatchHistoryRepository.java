package info.ggamt.gest.repository.baram.macro;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ggamt.gest.domain.baram.macro.MacroBatchHistory;

@Repository
public interface MacroBatchHistoryRepository extends JpaRepository<MacroBatchHistory, Long> {

    Optional<MacroBatchHistory> findFirstByOrderByIdDesc();
}