package dev.amendola.appControleMoradores.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.amendola.appControleMoradores.Model.Responsavel;

@Repository
public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {
    Responsavel findByCpf(String cpf);
}