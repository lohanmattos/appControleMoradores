package dev.amendola.appControleMoradores.Controler;


import dev.amendola.appControleMoradores.Model.Responsavel;
import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Repository.UsuarioRepository;
import dev.amendola.appControleMoradores.Service.ResponsavelService;
import dev.amendola.appControleMoradores.Service.UsuarioService;

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
    private UsuarioService usuarioService; 
    
    @GetMapping
    public String listarResponsaveis(Model model) {
        model.addAttribute("responsaveis", responsavelService.listarTodos());
        return "responsaveis/listar";
    }
    
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Responsavel responsavel = responsavelService.buscarPorId(id);
        model.addAttribute("usuarioResponsavel", responsavel);
        return "responsaveis/detalhes";
    }
    
    @GetMapping("/editar/{id}")
    public String preEditarResponsavel(@PathVariable String id, Model model) {
        // Busca o responsável associado ao ID do usuário
        Responsavel responsavel = responsavelService.findByUsuarioId(id);

        if (responsavel == null) {
            // Cria um novo responsável caso não exista
            Usuario usuario = usuarioService.OptbuscarUsuarioPorId(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
            responsavel = new Responsavel();
            responsavel.setUsuario(usuario);
        }

        // Adiciona o responsável ao modelo
        model.addAttribute("responsavel", responsavel);
        return "responsaveis/formulario";
    }


    @PostMapping("/salvar")
    public String CadastrarResponsavel(Responsavel responsavel, Model model) {
        if (responsavel.getUsuario() != null && responsavel.getUsuario().getId() != null) {
            // Busca o usuário associado no banco de dados
            Usuario usuarioExistente = usuarioService.OptbuscarUsuarioPorId(responsavel.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + responsavel.getUsuario().getId()));
            // Mantém o usuário existente associado ao responsável
            responsavel.setUsuario(usuarioExistente);
        }

        // Salva ou atualiza o responsável
        responsavelService.salvarResponsavel(responsavel);

        return "redirect:/responsaveis";
    }

    
    @GetMapping("/novo")
    public String novoUsuarioResponsavel(Model model) {
        model.addAttribute("responsavel", new Responsavel());
       
        
        return "responsaveis/formulario";
    }

}
