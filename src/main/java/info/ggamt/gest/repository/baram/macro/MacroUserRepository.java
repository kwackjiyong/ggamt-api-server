package info.ggamt.gest.repository.baram.macro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ggamt.gest.domain.baram.macro.MacroUser;

@Repository
public interface MacroUserRepository extends JpaRepository<MacroUser, Long> {

    public long countByUserName(String userName);
}