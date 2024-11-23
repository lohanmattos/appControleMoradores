package dev.amendola.appControleMoradores.Repository;

import dev.amendola.appControleMoradores.Model.Receita;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
	@Query("SELECT SUM(r.valor) FROM Receita r")
    BigDecimal calcularTotalReceitas();
}