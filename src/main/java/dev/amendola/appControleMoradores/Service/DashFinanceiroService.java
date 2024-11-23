package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Repository.ReceitaRepository;
import dev.amendola.appControleMoradores.DTO.MovimentacaoDTO;
import dev.amendola.appControleMoradores.Repository.DespesaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DashFinanceiroService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    public BigDecimal calcularTotalReceitas() {
    	 return Optional.ofNullable(receitaRepository.calcularTotalReceitas()).orElse(BigDecimal.ZERO);
    }

    public BigDecimal calcularTotalDespesas() {
        return Optional.ofNullable(despesaRepository.calcularTotalDespesas()).orElse(BigDecimal.ZERO);

    }

    public BigDecimal calcularLucro() {
        return calcularTotalReceitas().subtract(calcularTotalDespesas());
    }

    public List<MovimentacaoDTO> listarMovimentacoes() {
        List<MovimentacaoDTO> movimentacoes = new ArrayList<>();

        receitaRepository.findAll().forEach(receita -> {
            movimentacoes.add(new MovimentacaoDTO(
                "Receita",
                receita.getDescricao(),
                receita.getValor(),
                receita.getData()
            ));
        });

        despesaRepository.findAll().forEach(despesa -> {
            movimentacoes.add(new MovimentacaoDTO(
                "Despesa",
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData()
            ));
        });

        return movimentacoes;
    }
}
