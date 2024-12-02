package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.UsuarioResponsavel;
import dev.amendola.appControleMoradores.Repository.UsuarioResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioResponsavelService {

    @Autowired
    private UsuarioResponsavelRepository responsavelRepository;

    public List<UsuarioResponsavel> listarTodos() {
        return responsavelRepository.findAll();
    }

    public UsuarioResponsavel buscarPorId(Long id) {
        return responsavelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário Responsável não encontrado."));
    }

    public void salvar(UsuarioResponsavel responsavel) {
        responsavelRepository.save(responsavel);
    }

    public void excluir(Long id) {
        if (responsavelRepository.existsById(id)) {
            responsavelRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário Responsável não encontrado.");
        }
    }
}
