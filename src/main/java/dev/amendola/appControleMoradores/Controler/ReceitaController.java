package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Service.ReceitaService;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }
   

    // Exibe a lista de receitas e inicializa o formulário no modal
    @GetMapping
    public String listarReceitas(Model model) {
        model.addAttribute("receitas", receitaService.listarTodas());
        model.addAttribute("receita", new Receita()); // Inicializa o objeto para o modal

        return "financeiro/receitas/lista"; // Redireciona para o template receitas/lista.html
    }

    
    @PostMapping
    public String cadastrarReceita(@ModelAttribute("receita") Receita receita, RedirectAttributes redirectAttributes) {
        try {
            receitaService.salvar(receita);
            redirectAttributes.addAttribute("success", "Receita cadastrada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao cadastrar a receita.");
        }
        return "redirect:/receitas";
    }
    
   
    
    @GetMapping("/excluir/{id}")
    public String excluirDespesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            receitaService.deletar(id);
            redirectAttributes.addAttribute("success", "Receita excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao excluir a receita.");
        }
        return "redirect:/receitas";
    }
}
