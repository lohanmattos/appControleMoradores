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
        model.addAttribute("morador", new Morador());

        return "morador/moradores";
    }

    @PostMapping
    public String cadastrarMorador(@ModelAttribute Morador morador, RedirectAttributes attributes) {
        try {
            moradorService.salvar(morador);
            attributes.addFlashAttribute("success", "Morador cadastrado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Erro ao cadastrar morador.");
        }
        return "redirect:/moradores";
    }

    @GetMapping("/editar/{id}")
    public String editarMorador(@PathVariable Long id, Model model) {
        Morador morador = moradorService.buscarPorCpf(id.toString());
        model.addAttribute("morador", morador);
        return "morador/formulario-morador";
    }

    @GetMapping("/excluir/{id}")
    public String excluirMorador(@PathVariable Long id) {
        moradorService.excluir(id);
        return "redirect:/moradores";
    }
}
