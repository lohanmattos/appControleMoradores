package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Model.Despesa;
import dev.amendola.appControleMoradores.Model.Receita;
import dev.amendola.appControleMoradores.Model.TipoCategoria;
import dev.amendola.appControleMoradores.Service.CategoriaContasService;
import dev.amendola.appControleMoradores.Service.DespesaService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;
    
    @Autowired
    private CategoriaContasService categoriaContasService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public String listarDespesas(Model model) {
        model.addAttribute("despesas", despesaService.listarTodas());
        model.addAttribute("despesa", new Despesa()); // Para o modal
        
        List<CategoriaContas> categoriasDespesas = categoriaContasService.listarPorTipo(TipoCategoria.DESPESA);

        model.addAttribute("categoriasDespesas", categoriasDespesas);
        return "financeiro/despesas/despesas";
    }

 // Exemplo de controlador em Spring
    @PostMapping
    public String cadastrarDespesa(@ModelAttribute("despesa") Despesa despesa, RedirectAttributes redirectAttributes) {
        try {
            despesaService.salvar(despesa);
            redirectAttributes.addAttribute("success", "Despesa cadastrada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao cadastrar a despesa.");
        }
        return "redirect:/despesas";
    }

    @GetMapping("/excluir/{id}")
    public String excluirDespesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            despesaService.deletar(id);
            redirectAttributes.addAttribute("success", "Despesa excluída com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erro ao excluir a despesa.");
        }
        return "redirect:/despesas";
    }
    
    @GetMapping("/filtro")
    public String listarDespesas(@RequestParam(required = false) LocalDate dataInicio,
                                 @RequestParam(required = false) LocalDate dataFim,
                                 @RequestParam(required = false) Long categoria,
                                 @RequestParam(required = false) Boolean pago,
                                 Model model) {

        // Obter todas as despesas
        List<Despesa> todasDespesas = despesaService.listarTodas();

        // Aplicar filtros apenas se os parâmetros forem fornecidos
        List<Despesa> despesasFiltradas = todasDespesas.stream()
                .filter(despesa -> (dataInicio == null || !despesa.getData().isBefore(dataInicio)))
                .filter(despesa -> (dataFim == null || !despesa.getData().isAfter(dataFim)))
                .filter(despesa -> (categoria == null || (despesa.getCategoria() != null && despesa.getCategoria().getId().equals(categoria))))
                .filter(despesa -> (pago == null || despesa.isPago() == pago))
                .collect(Collectors.toList());

        // Passar dados para o modelo
        model.addAttribute("despesas", despesasFiltradas.isEmpty() ? todasDespesas : despesasFiltradas);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("categoria", categoria);
        model.addAttribute("pago", pago);
        model.addAttribute("categoriasDespesas", categoriaContasService.listarTodos());
        
        // Adiciona um novo objeto Receita ao modelo para o formulário
        model.addAttribute("despesa", new Despesa());

        return "financeiro/despesas/despesas"; // Nome do arquivo HTML
    }

}
