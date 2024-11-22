package dev.amendola.appControleMoradores.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeControler {

    @GetMapping("/home")
    public String home(Model model) {
        // Título e Descrição
        model.addAttribute("titulo", "Bem-vindo ao Sistema de Controle de Condomínio");
        model.addAttribute("descricao", "Gerencie receitas, despesas e relatórios financeiros de forma simples e eficiente.");

        // Requisitos do Sistema
        List<String> requisitos = Arrays.asList(
                "Cadastro de receitas com descrição e valor.",
                "Cadastro de despesas com descrição e valor.",
                "Geração de relatórios financeiros detalhados.",
                "Interface amigável e responsiva utilizando Bootstrap.",
                "Gerenciamento de receitas e despesas acessível através de menus intuitivos."
        );
        model.addAttribute("requisitos", requisitos);

        return "home";
    }
}