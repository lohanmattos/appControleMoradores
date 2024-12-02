package dev.amendola.appControleMoradores.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.amendola.appControleMoradores.Model.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByEmail(String email);
}
