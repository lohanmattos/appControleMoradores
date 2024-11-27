package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Service.CategoriaContasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaContasController {
	
	@Autowired
    private CategoriaContasService categoriaContasService;

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaContasService.listarTodos());
        model.addAttribute("categoria", new CategoriaContas()); // Adiciona o objeto ao modelo

        return "financeiro/categoriaContas/categoria-contas"; // Crie um template para exibir as categorias
    }
    
    @PostMapping
    public String salvarCategoria(@ModelAttribute("categoria") CategoriaContas categoriacontas, RedirectAttributes redirectAttributes) {
        try {
        	categoriaContasService.salvar(categoriacontas);
            redirectAttributes.addAttribute("success", "Categoria salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao salvar a categoria.");
        }
        return "redirect:/categorias";
    }
    
    @GetMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
        	categoriaContasService.deletar(id);
            redirectAttributes.addAttribute("success", "Categoria excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao excluir a categoria.");
        }
        return "redirect:/categorias";
    }
    
}
