package dev.amendola.appControleMoradores.Repository;

import dev.amendola.appControleMoradores.Model.Despesa;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
	@Query("SELECT SUM(d.valor) FROM Despesa d")
    BigDecimal calcularTotalDespesas();
}