package dev.amendola.appControleMoradores.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceiroControler {
	
	@GetMapping("/financeiro")
    public String home(Model model) {
        model.addAttribute("titulo", "Bem-vindo ao Sistema de Controle de Condomínio");
        model.addAttribute("descricao", "Gerencie receitas, despesas e muito mais de forma eficiente.");
        return "home";
    }

}
