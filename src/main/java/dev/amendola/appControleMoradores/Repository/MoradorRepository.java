package dev.amendola.appControleMoradores.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.amendola.appControleMoradores.Model.Morador;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long> {
    Morador findByCpf(String cpf);
}