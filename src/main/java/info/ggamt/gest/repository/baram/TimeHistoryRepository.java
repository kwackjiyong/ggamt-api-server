package info.ggamt.gest.repository.baram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ggamt.gest.domain.baram.TimeHistory;


@Repository
public interface TimeHistoryRepository extends JpaRepository<TimeHistory, Long> {}
