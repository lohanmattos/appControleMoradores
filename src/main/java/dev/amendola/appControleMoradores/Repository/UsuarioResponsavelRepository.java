package dev.amendola.appControleMoradores.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.amendola.appControleMoradores.Model.UsuarioResponsavel;

@Repository
public interface UsuarioResponsavelRepository extends JpaRepository<UsuarioResponsavel, Long> {
    UsuarioResponsavel findByCpf(String cpf);
}