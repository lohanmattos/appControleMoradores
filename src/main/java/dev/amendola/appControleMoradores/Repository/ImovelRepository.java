package dev.amendola.appControleMoradores.Repository;

import dev.amendola.appControleMoradores.Model.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {
}
