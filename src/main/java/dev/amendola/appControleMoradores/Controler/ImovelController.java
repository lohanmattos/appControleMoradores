package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.Imovel;
import dev.amendola.appControleMoradores.Model.UsuarioResponsavel;
import dev.amendola.appControleMoradores.Service.ImovelService;
import dev.amendola.appControleMoradores.Service.UsuarioResponsavelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;
    
    @Autowired
    private UsuarioResponsavelService responsavelService;

    @GetMapping
    public String listarTodos(Model model) {
        List<Imovel> imoveis = imovelService.listarTodos();
        model.addAttribute("imoveis", imoveis);
        return "imoveis/listar";
    }

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Imovel imovel = imovelService.buscarPorId(id);
        model.addAttribute("imovel", imovel);
        return "imoveis/detalhes";
    }

    @GetMapping("/novo")
    public String novoImovel(Model model) {
        model.addAttribute("imovel", new Imovel());
        model.addAttribute("responsaveis", responsavelService.listarTodos());

        return "imoveis/formulario";
    }

    @PostMapping
    public String salvar(@ModelAttribute Imovel imovel) {
        if (imovel.getResponsavel() != null && imovel.getResponsavel().getId() != null) {
            UsuarioResponsavel responsavel = responsavelService.buscarPorId(imovel.getResponsavel().getId());
            imovel.setResponsavel(responsavel); // Associa o responsável, se fornecido
        } else {
            imovel.setResponsavel(null); // Permite imóvel sem responsável
        }
        imovelService.salvar(imovel);
        return "redirect:/imoveis";
    }


    @GetMapping("/editar/{id}")
    public String editarImovel(@PathVariable Long id, Model model) {
        Imovel imovel = imovelService.buscarPorId(id);
        model.addAttribute("responsaveis", responsavelService.listarTodos());

        model.addAttribute("imovel", imovel);
        return "imoveis/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Imovel imovelAtualizado) {
        imovelService.atualizar(id, imovelAtualizado);
        return "redirect:/imoveis";
    }

    @GetMapping("/excluir/{id}")
    public String deletar(@PathVariable Long id) {
        imovelService.deletar(id);
        return "redirect:/imoveis";
    }
}
