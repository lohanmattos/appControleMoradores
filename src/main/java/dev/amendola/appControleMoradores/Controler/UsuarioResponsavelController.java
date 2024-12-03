package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Imovel;
import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.UsuarioResponsavel;
import dev.amendola.appControleMoradores.Repository.PerfilRepository;
import dev.amendola.appControleMoradores.Service.UsuarioResponsavelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuariosResponsaveis")
public class UsuarioResponsavelController {

    @Autowired
    private UsuarioResponsavelService responsavelService;
    
    @Autowired
    private PerfilRepository perfilRepository;

    @GetMapping
    public String listarUsuariosResponsaveis(Model model) {
        model.addAttribute("usuariosResponsaveis", responsavelService.listarTodos());
        return "usuariosResponsaveis/listar";
    }
    
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        UsuarioResponsavel usuarioResponsavel = responsavelService.buscarPorId(id);
        model.addAttribute("usuarioResponsavel", usuarioResponsavel);
        return "usuariosResponsaveis/detalhes";
    }

    @GetMapping("/novo")
    public String novoUsuarioResponsavel(Model model) {
        model.addAttribute("usuarioResponsavel", new UsuarioResponsavel());
        
        // Adiciona a lista de perfis disponíveis ao modelo
        List<Perfil> perfis = perfilRepository.findAll();
        
        model.addAttribute("perfis", perfis);
        
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
        // Adiciona a lista de perfis disponíveis ao modelo
        List<Perfil> perfis = perfilRepository.findAll();
        
        model.addAttribute("perfis", perfis);
        return "usuariosResponsaveis/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuarioResponsavel(@PathVariable Long id) {
        responsavelService.excluir(id);
        return "redirect:/usuariosResponsaveis";
    }
}
