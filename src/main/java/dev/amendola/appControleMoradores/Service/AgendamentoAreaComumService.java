package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.AgendamentoAreaComum;
import dev.amendola.appControleMoradores.Repository.AgendamentoAreaComumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoAreaComumService {

    @Autowired
    private AgendamentoAreaComumRepository repository;

    public List<AgendamentoAreaComum> listarTodos() {
        return repository.findAll();
    }

    public AgendamentoAreaComum buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com ID: " + id));
    }

    public AgendamentoAreaComum salvar(AgendamentoAreaComum agendamento) {
        return repository.save(agendamento);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
