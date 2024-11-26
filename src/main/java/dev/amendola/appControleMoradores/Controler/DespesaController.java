package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Model.Despesa;
import dev.amendola.appControleMoradores.Model.TipoCategoria;
import dev.amendola.appControleMoradores.Service.CategoriaContasService;
import dev.amendola.appControleMoradores.Service.DespesaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;
    
    @Autowired
    private CategoriaContasService categoriaContasService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public String listarDespesas(Model model) {
        model.addAttribute("despesas", despesaService.listarTodas());
        model.addAttribute("despesa", new Despesa()); // Para o modal
        
        List<CategoriaContas> categoriasDespesas = categoriaContasService.listarPorTipo(TipoCategoria.DESPESA);

        model.addAttribute("categoriasDespesas", categoriasDespesas);
        return "financeiro/despesas/lista";
    }

 // Exemplo de controlador em Spring
    @PostMapping
    public String cadastrarDespesa(@ModelAttribute("despesa") Despesa despesa, RedirectAttributes redirectAttributes) {
        try {
            despesaService.salvar(despesa);
            redirectAttributes.addAttribute("success", "Despesa cadastrada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao cadastrar a despesa.");
        }
        return "redirect:/despesas";
    }

    @GetMapping("/excluir/{id}")
    public String excluirDespesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            despesaService.deletar(id);
            redirectAttributes.addAttribute("success", "Despesa excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao excluir a despesa.");
        }
        return "redirect:/despesas";
    }
}
