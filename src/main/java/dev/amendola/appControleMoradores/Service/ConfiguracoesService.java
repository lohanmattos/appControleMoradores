package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.Configuracoes;
import dev.amendola.appControleMoradores.Repository.ConfiguracoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracoesService {

    @Autowired
    private ConfiguracoesRepository configuracoesRepository;

    public Configuracoes buscarConfiguracoes() {
        return configuracoesRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Configurações não encontradas!"));
    }

    public Configuracoes salvarConfiguracoes(Configuracoes configuracoes) {
        return configuracoesRepository.save(configuracoes);
    }
}
