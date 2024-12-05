package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.AreaComum;
import dev.amendola.appControleMoradores.Repository.AreaComumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaComumService {

    private final AreaComumRepository repository;

    public AreaComumService(AreaComumRepository repository) {
        this.repository = repository;
    }

    public List<AreaComum> listarTodas() {
        return repository.findAll();
    }

    public Optional<AreaComum> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public AreaComum salvar(AreaComum areaComum) {
        return repository.save(areaComum);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
