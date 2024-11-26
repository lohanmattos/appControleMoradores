package dev.amendola.appControleMoradores.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Model.TipoCategoria;

@Repository
public interface CategoriaContasRepository extends JpaRepository<CategoriaContas, Long> {
    List<CategoriaContas> findByTipo(TipoCategoria tipo);

}
