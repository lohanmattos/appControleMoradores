package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Repository.ReceitaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReceitaService {

	@Autowired
    private final ReceitaRepository receitaRepository;
   
    public ReceitaService(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    public List<Receita> listarTodas() {
        return receitaRepository.findAll();
    }

    public Receita salvar(Receita receita) {
        return receitaRepository.save(receita);
    }

    public void deletar(Long id) {
        receitaRepository.deleteById(id);
    }

}