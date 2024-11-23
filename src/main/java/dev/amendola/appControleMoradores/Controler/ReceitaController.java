package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Service.ReceitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // Salva uma nova receita
    @PostMapping
    public String salvarReceita(@ModelAttribute Receita receita) {
        receitaService.salvar(receita);
        return "redirect:/receitas"; // Redireciona para a lista de receitas após salvar
    }

    // Remove uma receita
    @GetMapping("/excluir/{id}")
    public String excluirReceita(@PathVariable Long id) {
        receitaService.deletar(id);
        return "redirect:/receitas"; // Redireciona para a lista de receitas após excluir
    }
}
