package info.ggamt.gest.repository.baram.macro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ggamt.gest.domain.baram.macro.MacroMLive;

@Repository
public interface MacroMLiveRepository extends JpaRepository<MacroMLive, Long> {}