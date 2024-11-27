package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Configuracoes;
import dev.amendola.appControleMoradores.Service.ConfiguracoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConfiguracoesController {

    @Autowired
    private ConfiguracoesService configuracoesService;

    // Exibe o formulário com as configurações atuais
    @GetMapping("/configuracoes")
    public String exibirConfiguracoes(Model model) {
        try {
            Configuracoes configuracoes = configuracoesService.buscarConfiguracoes();
            model.addAttribute("configuracoes", configuracoes);
        } catch (RuntimeException e) {
            model.addAttribute("configuracoes", new Configuracoes());
        }
        return "configuracoes/config";
    }

    // Salva as alterações realizadas no formulário
    @PostMapping("/configuracoes")
    public String salvarConfiguracoes(Configuracoes configuracoes) {
        configuracoesService.editarConfiguracoes(configuracoes);
        return "redirect:/configuracoes?sucesso=true";
    }
}
