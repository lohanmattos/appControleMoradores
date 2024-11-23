package dev.amendola.appControleMoradores.Repository;

import dev.amendola.appControleMoradores.Model.Configuracoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracoesRepository extends JpaRepository<Configuracoes, Long> {
}