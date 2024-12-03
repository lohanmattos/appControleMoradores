package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Model.TipoCategoria;
import dev.amendola.appControleMoradores.Service.CategoriaContasService;
import dev.amendola.appControleMoradores.Service.ReceitaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.amendola.appControleMoradores.Model.CategoriaContas;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;
    
    @Autowired
    private CategoriaContasService categoriaContasService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }
   

    // Exibe a lista de receitas e inicializa o formulário no modal
    @GetMapping
    public String listarReceitas(Model model) {
        model.addAttribute("receitas", receitaService.listarTodas());
        model.addAttribute("receita", new Receita()); // Inicializa o objeto para o modal

        List<CategoriaContas> categoriasReceitas = categoriaContasService.listarPorTipo(TipoCategoria.RECEITA);

        model.addAttribute("categoriasReceitas", categoriasReceitas);
        
        return "financeiro/receitas/receitas"; // Redireciona para o template receitas/lista.html
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
