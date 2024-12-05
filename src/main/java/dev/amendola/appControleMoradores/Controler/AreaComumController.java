package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.AreaComum;
import dev.amendola.appControleMoradores.Service.AreaComumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/areaComum")
public class AreaComumController {

    private final AreaComumService service;

    public AreaComumController(AreaComumService service) {
        this.service = service;
    }

    @GetMapping
    public String listarAreasComuns(Model model) {
        model.addAttribute("areaComum", service.listarTodas());
        return "areaComum/lista";
    }

    @GetMapping("/novo")
    public String novaAreaComum(Model model) {
        model.addAttribute("areaComum", new AreaComum());
        return "areaComum/formulario";
    }

    @PostMapping
    public String salvarAreaComum(@ModelAttribute AreaComum areaComum) {
        service.salvar(areaComum);
        return "redirect:/areaComum";
    }

    @GetMapping("/editar/{id}")
    public String editarAreaComum(@PathVariable Long id, Model model) {
        AreaComum areaComum = service.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Área Comum não encontrada com ID: " + id));
        model.addAttribute("areaComum", areaComum);
        return "areaComum/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirAreaComum(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/areaComum";
    }
}
