package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.AgendamentoAreaComum;
import dev.amendola.appControleMoradores.Service.AgendamentoAreaComumService;
import dev.amendola.appControleMoradores.Service.AreaComumService;
import dev.amendola.appControleMoradores.Service.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoAreaComumController {

    @Autowired
    private AgendamentoAreaComumService agendamentoService;

    @Autowired
    private ResponsavelService responsavelService;

    @Autowired
    private AreaComumService areaComumService;

    @GetMapping
    public String listarAgendamentos(Model model) {
        model.addAttribute("agendamentos", agendamentoService.listarTodos());
        return "agendamentos/lista";
    }

    @GetMapping("/novo")
    public String novoAgendamento(Model model) {
        model.addAttribute("agendamento", new AgendamentoAreaComum());
        model.addAttribute("responsaveis", responsavelService.listarTodos());
        model.addAttribute("areasComuns", areaComumService.listarTodas());
        return "agendamentos/formulario";
    }

    @PostMapping
    public String salvarAgendamento(@ModelAttribute AgendamentoAreaComum agendamento) {
        if (agendamento.getId() != null) {
            AgendamentoAreaComum agendamentoExistente = agendamentoService.buscarPorId(agendamento.getId());
            agendamentoExistente.setDataInicio(agendamento.getDataInicio());
            agendamentoExistente.setDataFim(agendamento.getDataFim());
            agendamentoExistente.setResponsavel(agendamento.getResponsavel());
            agendamentoExistente.setAreaComum(agendamento.getAreaComum());
            agendamentoExistente.setObservacao(agendamento.getObservacao());
            agendamentoService.salvar(agendamentoExistente);
        } else {
            agendamentoService.salvar(agendamento);
        }
        return "redirect:/agendamentos";
    }


    @GetMapping("/editar/{id}")
    public String editarAgendamento(@PathVariable Long id, Model model) {
        AgendamentoAreaComum agendamento = agendamentoService.buscarPorId(id);
        
        if (agendamento == null) {
            throw new RuntimeException("Agendamento não encontrado com ID: " + id);
        }
        
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("responsaveis", responsavelService.listarTodos());
        model.addAttribute("areasComuns", areaComumService.listarTodas());
        return "agendamentos/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirAgendamento(@PathVariable Long id) {
        agendamentoService.excluir(id);
        return "redirect:/agendamentos";
    }
    
    
}
