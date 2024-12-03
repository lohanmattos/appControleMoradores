package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Model.Responsavel;
import dev.amendola.appControleMoradores.Repository.PerfilRepository;
import dev.amendola.appControleMoradores.Repository.UsuarioRepository;
import dev.amendola.appControleMoradores.Service.ResponsavelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/responsaveis")
public class ResponsavelController {

    @Autowired
    private ResponsavelService responsavelService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PerfilRepository perfilRepository;

    @GetMapping
    public String listarUsuariosResponsaveis(Model model) {
        model.addAttribute("usuariosResponsaveis", responsavelService.listarTodos());
        return "responsaveis/listar";
    }
    
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Responsavel responsavel = responsavelService.buscarPorId(id);
        model.addAttribute("usuarioResponsavel", responsavel);
        return "responsaveis/detalhes";
    }

    @GetMapping("/novo")
    public String novoUsuarioResponsavel(Model model) {
        model.addAttribute("usuarioResponsavel", new Responsavel());
        
        // Adiciona a lista de perfis disponíveis ao modelo
        List<Perfil> perfis = perfilRepository.findAll();
        
        model.addAttribute("perfis", perfis);
        
        return "responsaveis/formulario";
    }

    @PostMapping
    public String salvarUsuarioResponsavel(@ModelAttribute Responsavel responsavel) {
        responsavelService.salvar(responsavel);
        return "redirect:/responsaveis";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuarioResponsavel(@PathVariable String id, Model model) {
      
    	
        model.addAttribute("usuarioResponsavel", new Responsavel());
        // Adiciona a lista de perfis disponíveis ao modelo
        List<Perfil> perfis = perfilRepository.findAll();
        
        model.addAttribute("perfis", perfis);
        return "responsaveis/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuarioResponsavel(@PathVariable Long id) {
        responsavelService.excluir(id);
        return "redirect:/responsaveis";
    }
}
