package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.UsuarioResponsavel;
import dev.amendola.appControleMoradores.Service.UsuarioResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuariosResponsaveis")
public class UsuarioResponsavelController {

    @Autowired
    private UsuarioResponsavelService responsavelService;

    @GetMapping
    public String listarUsuariosResponsaveis(Model model) {
        model.addAttribute("usuariosResponsaveis", responsavelService.listarTodos());
        return "usuariosResponsaveis/listar";
    }

    @GetMapping("/novo")
    public String novoUsuarioResponsavel(Model model) {
        model.addAttribute("usuarioResponsavel", new UsuarioResponsavel());
        return "usuariosResponsaveis/formulario";
    }

    @PostMapping
    public String salvarUsuarioResponsavel(@ModelAttribute UsuarioResponsavel usuarioResponsavel) {
        responsavelService.salvar(usuarioResponsavel);
        return "redirect:/usuariosResponsaveis";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuarioResponsavel(@PathVariable Long id, Model model) {
        model.addAttribute("usuarioResponsavel", responsavelService.buscarPorId(id));
        return "usuariosResponsaveis/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuarioResponsavel(@PathVariable Long id) {
        responsavelService.excluir(id);
        return "redirect:/usuariosResponsaveis";
    }
}
