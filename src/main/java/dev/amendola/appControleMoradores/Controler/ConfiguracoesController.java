package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Configuracoes;
import dev.amendola.appControleMoradores.Service.ConfiguracoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConfiguracoesController {

    @Autowired
    private ConfiguracoesService configuracoesService;

    @GetMapping("/conf")
    public String exibirConfiguracoes(Model model) {
        Configuracoes configuracoes = configuracoesService.buscarConfiguracoes();
        model.addAttribute("configuracoes", configuracoes);
        return "configuracoes/form";
    }

    @PostMapping
    public String salvarConfiguracoes(@ModelAttribute Configuracoes configuracoes) {
        configuracoesService.salvarConfiguracoes(configuracoes);
        return "redirect:/configuracoes?sucesso=true";
    }
}
