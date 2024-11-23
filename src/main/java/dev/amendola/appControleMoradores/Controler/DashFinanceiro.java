package dev.amendola.appControleMoradores.Controler;

import dev.amendola.appControleMoradores.DTO.MovimentacaoDTO;
import dev.amendola.appControleMoradores.Service.DashFinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class DashFinanceiro {

    @Autowired
    private DashFinanceiroService financeiroService;

    @GetMapping("/dashFinanceiro")
    public String dashboard(Model model) {
        BigDecimal totalReceitas = financeiroService.calcularTotalReceitas();
        BigDecimal totalDespesas = financeiroService.calcularTotalDespesas();
        BigDecimal lucro = totalReceitas.subtract(totalDespesas);
        
        // Busca todas as movimentações financeiras (receitas e despesas)
        List<MovimentacaoDTO> movimentacoes = financeiroService.listarMovimentacoes();

        model.addAttribute("totalReceitas", String.format("R$ %.2f", totalReceitas));
        model.addAttribute("totalDespesas", String.format("R$ %.2f", totalDespesas));
        model.addAttribute("lucro", String.format("R$ %.2f", lucro));
        
        model.addAttribute("movimentacoes", movimentacoes);


        return "financeiro/dashFinanceiro";
    }
}
