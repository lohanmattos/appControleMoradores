package dev.amendola.appControleMoradores.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioControler {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Página Inicial");
        model.addAttribute("descricao", "Sistema para gerenciar a vila Militar de forma eficiente.");
        return "inicio";
    }
}