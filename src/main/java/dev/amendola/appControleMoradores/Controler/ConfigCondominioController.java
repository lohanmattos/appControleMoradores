package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.ConfigCondominio;
import dev.amendola.appControleMoradores.Model.Responsavel;
import dev.amendola.appControleMoradores.Service.ConfigCondominioService;
import dev.amendola.appControleMoradores.Service.ResponsavelService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConfigCondominioController {

    @Autowired
    private ConfigCondominioService configuracoesService;

    @Autowired
    private ResponsavelService responsavelService; 
    
    // Exibe o formulário com as configurações atuais
    @GetMapping("/configuracoes")
    public String exibirConfiguracoes(Model model) {
        try {
            ConfigCondominio configuracoes = configuracoesService.buscarConfiguracoes();
            List<Responsavel> responsaveis = responsavelService.listarTodos();
            
            model.addAttribute("responsaveis", this.filtrarResponsaveisSindico(responsaveis));
            model.addAttribute("configuracoes", configuracoes);
        } catch (RuntimeException e) {
            model.addAttribute("configuracoes", new ConfigCondominio());
        }
        return "configuracoes/config";
    }

    // Salva as alterações realizadas no formulário
    @PostMapping("/configuracoes")
    public String salvarConfiguracoes(ConfigCondominio configuracoes) {
    	
    	// Se o sindico não for configurado, trate isso adequadamente
        if (configuracoes.getSindico() == null) {
        	configuracoes.setSindico(null); // Ou alguma lógica padrão
        }
        
        configuracoesService.editarConfiguracoes(configuracoes);
        return "redirect:/configuracoes?sucesso=true";
    }
    
    public List<Responsavel> filtrarResponsaveisSindico(List<Responsavel> responsaveis) {
        return responsaveis.stream()
                .filter(responsavel -> responsavel.getUsuario() != null) // Verifica se há um usuário associado
                .filter(responsavel -> responsavel.getUsuario().getPerfis().stream()
                        .anyMatch(perfil -> "SINDICO".equalsIgnoreCase(perfil.getRole()))) // Verifica o perfil de síndico
                .collect(Collectors.toList());
    }
}
