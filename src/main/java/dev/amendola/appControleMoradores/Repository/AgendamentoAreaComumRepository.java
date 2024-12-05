package dev.amendola.appControleMoradores.Repository;

import dev.amendola.appControleMoradores.Model.AgendamentoAreaComum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoAreaComumRepository extends JpaRepository<AgendamentoAreaComum, Long> {
}
