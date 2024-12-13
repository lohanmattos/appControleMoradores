package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Service.CategoriaContasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaContasController {
	
	@Autowired
    private CategoriaContasService categoriaContasService;

	@GetMapping()
	public String listarCategorias(@RequestParam(defaultValue = "0") int page, 
	                               @RequestParam(defaultValue = "10") int size, 
	                               Model model) {

	    // Obtenha uma página de categorias
	    Page<CategoriaContas> categoriasPage = categoriaContasService.listarCategorias(PageRequest.of(page, size));

	    // Adicione os atributos necessários ao modelo
	    model.addAttribute("categorias", categoriasPage.getContent());
	    model.addAttribute("currentPage", categoriasPage.getNumber());
	    model.addAttribute("totalPages", categoriasPage.getTotalPages());
	    model.addAttribute("totalElements", categoriasPage.getTotalElements());
	    model.addAttribute("pageSize", size);

	 // Adicione um objeto Categoria vazio para o formulário
	    model.addAttribute("categoria", new CategoriaContas());
	    
	    return "financeiro/categoriaContas/categoria-contas"; // Retorne o nome do arquivo HTML
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
