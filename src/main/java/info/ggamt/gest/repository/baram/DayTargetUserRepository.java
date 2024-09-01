package info.ggamt.gest.repository.baram;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ggamt.gest.domain.baram.DayTargetUser;

@Repository
public interface DayTargetUserRepository extends JpaRepository<DayTargetUser, Long> {
    
    List<DayTargetUser> findByJobTp(long l);
}
