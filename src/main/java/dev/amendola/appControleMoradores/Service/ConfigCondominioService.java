package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.ConfigCondominio;
import dev.amendola.appControleMoradores.Repository.ConfigCondominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigCondominioService {

    @Autowired
    private ConfigCondominioRepository configuracoesRepository;

    // Busca a configuração existente
    public ConfigCondominio buscarConfiguracoes() {
        return configuracoesRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Configurações não encontradas!"));
    }

    // Edita ou cria a configuração única
    public ConfigCondominio editarConfiguracoes(ConfigCondominio configuracoes) {
        // Garante que a configuração tenha o ID fixo de 1L
        configuracoes.setId(1L);
        return configuracoesRepository.save(configuracoes);
    }
}
