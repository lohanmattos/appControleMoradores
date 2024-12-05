package dev.amendola.appControleMoradores.Repository;

import dev.amendola.appControleMoradores.Model.AreaComum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaComumRepository extends JpaRepository<AreaComum, Long> {
}