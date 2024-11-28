package dev.amendola.appControleMoradores.Controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.amendola.appControleMoradores.Model.Morador;
import dev.amendola.appControleMoradores.Service.MoradorService;

@Controller
@RequestMapping("/moradores")
public class MoradorController {

    @Autowired
    private MoradorService moradorService;

    @GetMapping
    public String listarMoradores(Model model) {
        List<Morador> moradores = moradorService.buscarTodos();
        model.addAttribute("moradores", moradores);
        model.addAttribute("morador", new Morador()); // Para o modal de cadastro

        return "morador/moradores";
    }

    @PostMapping
    public String cadastrarOuAtualizarMorador(@ModelAttribute Morador morador, RedirectAttributes attributes) {
        try {
            moradorService.salvar(morador);
            if (morador.getId() == null) {
                attributes.addFlashAttribute("success", "Morador cadastrado com sucesso!");
            } else {
                attributes.addFlashAttribute("success", "Morador atualizado com sucesso!");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Erro ao salvar morador.");
        }
        return "redirect:/moradores";
    }

    @GetMapping("/editar/{id}")
    public String editarMorador(@PathVariable Long id, Model model) {
        Morador morador = moradorService.buscarPorId(id);
        model.addAttribute("morador", morador);
        return "morador/moradores";
    }

    @GetMapping("/excluir/{id}")
    public String excluirMorador(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            moradorService.excluir(id);
            attributes.addFlashAttribute("success", "Morador excluído com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Erro ao excluir morador.");
        }
        return "redirect:/moradores";
    }
}
