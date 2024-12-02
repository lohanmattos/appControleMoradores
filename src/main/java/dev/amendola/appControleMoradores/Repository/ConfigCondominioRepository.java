package dev.amendola.appControleMoradores.Repository;

import dev.amendola.appControleMoradores.Model.ConfigCondominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigCondominioRepository extends JpaRepository<ConfigCondominio, Long> {
}