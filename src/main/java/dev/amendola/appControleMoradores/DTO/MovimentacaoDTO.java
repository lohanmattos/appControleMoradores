package dev.amendola.appControleMoradores.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoDTO {
    private String tipo; // Receita ou Despesa
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

    public MovimentacaoDTO(String tipo, String descricao, BigDecimal valor, LocalDate data) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }
}
