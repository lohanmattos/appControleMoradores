package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.Configuracoes;
import dev.amendola.appControleMoradores.Repository.ConfiguracoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracoesService {

    @Autowired
    private ConfiguracoesRepository configuracoesRepository;

    // Busca a configuração existente
    public Configuracoes buscarConfiguracoes() {
        return configuracoesRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Configurações não encontradas!"));
    }

    // Edita ou cria a configuração única
    public Configuracoes editarConfiguracoes(Configuracoes configuracoes) {
        // Garante que a configuração tenha o ID fixo de 1L
        configuracoes.setId(1L);
        return configuracoesRepository.save(configuracoes);
    }
}
