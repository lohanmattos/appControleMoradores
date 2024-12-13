package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.DTO.MovimentacaoDTO;
import dev.amendola.appControleMoradores.Service.DashFinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashFinanceiro {

    @Autowired
    private DashFinanceiroService financeiroService;

    @GetMapping("/dashFinanceiro")
    public String dashboard(Model model) {
    	model.addAttribute("titulo", "Painel Financeiro");
         
        BigDecimal totalReceitas = financeiroService.calcularTotalReceitas();
        BigDecimal totalDespesas = financeiroService.calcularTotalDespesas();
        BigDecimal lucro = totalReceitas.subtract(totalDespesas);
        
        // Busca todas as movimentações financeiras (receitas e despesas)
        List<MovimentacaoDTO> movimentacoes = financeiroService.listarMovimentacoes();

        model.addAttribute("totalReceitas", String.format("R$ %.2f", totalReceitas));
        model.addAttribute("totalDespesas", String.format("R$ %.2f", totalDespesas));
        model.addAttribute("lucro", String.format("R$ %.2f", lucro));
        
        model.addAttribute("lucroStatus", lucro);

        
        model.addAttribute("movimentacoes", movimentacoes);


        return "financeiro/dashFinanceiro";
    }
    
    @GetMapping("/dashboard/financeiro")
    public String dashboardFinanceiro(@RequestParam(required = false) Integer mes,
                                      @RequestParam(required = false) String tipo,
                                      Model model) {
        // Simular a lista de movimentações (normalmente você buscaria do banco de dados)
        List<MovimentacaoDTO> todasMovimentacoes = List.of(
            new MovimentacaoDTO("Receita", "Venda de Produto", new BigDecimal("1200.00"), LocalDate.of(2024, 1, 15)),
            new MovimentacaoDTO("Despesa", "Compra de Material", new BigDecimal("450.00"), LocalDate.of(2024, 2, 10)),
            new MovimentacaoDTO("Receita", "Consultoria", new BigDecimal("800.00"), LocalDate.of(2024, 3, 5))
        );

        // Filtrar por mês (se o filtro for fornecido)
        List<MovimentacaoDTO> movimentacoesFiltradas = todasMovimentacoes.stream()
            .filter(mov -> (mes == null || mov.getData().getMonthValue() == mes))
            .filter(mov -> (tipo == null || tipo.isEmpty() || mov.getTipo().equalsIgnoreCase(tipo)))
            .collect(Collectors.toList());

        // Passar dados filtrados para o modelo
        model.addAttribute("movimentacoes", movimentacoesFiltradas);
        model.addAttribute("mes", mes);
        model.addAttribute("tipo", tipo);

        // Dados do resumo
        model.addAttribute("totalReceitas", movimentacoesFiltradas.stream()
            .filter(mov -> mov.getTipo().equalsIgnoreCase("Receita"))
            .map(MovimentacaoDTO::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        model.addAttribute("totalDespesas", movimentacoesFiltradas.stream()
            .filter(mov -> mov.getTipo().equalsIgnoreCase("Despesa"))
            .map(MovimentacaoDTO::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        model.addAttribute("lucro", movimentacoesFiltradas.stream()
            .map(mov -> mov.getTipo().equalsIgnoreCase("Receita") ? mov.getValor() : mov.getValor().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        return "financeiro/dashFinanceiro"; // Nome do arquivo Thymeleaf
    }
}
