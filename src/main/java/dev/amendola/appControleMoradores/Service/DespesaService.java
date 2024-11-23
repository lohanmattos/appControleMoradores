package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.Despesa;
import dev.amendola.appControleMoradores.Repository.DespesaRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;

    public DespesaService(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    public List<Despesa> listarTodas() {
        return despesaRepository.findAll();
    }

    public Despesa salvar(Despesa despesa) {
        return despesaRepository.save(despesa);
    }

    public void deletar(Long id) {
        despesaRepository.deleteById(id);
    }
}