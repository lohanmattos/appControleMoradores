package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Despesa;
import dev.amendola.appControleMoradores.Service.DespesaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public String listarDespesas(Model model) {
        model.addAttribute("despesas", despesaService.listarTodas());
        model.addAttribute("despesa", new Despesa()); // Para o modal
        return "financeiro/despesas/lista";
    }

    @PostMapping
    public String salvarDespesa(@ModelAttribute Despesa despesa) {
        despesaService.salvar(despesa);
        return "redirect:/despesas";
    }

    @GetMapping("/excluir/{id}")
    public String excluirDespesa(@PathVariable Long id) {
        despesaService.deletar(id);
        return "redirect:/despesas";
    }
}
