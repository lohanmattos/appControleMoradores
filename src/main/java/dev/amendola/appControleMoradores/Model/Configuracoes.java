package dev.amendola.appControleMoradores.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "configuracoes")
public class Configuracoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeCondominio;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(nullable = false, length = 50)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false, length = 100)
    private String sindico;

    @Column(nullable = true)
    private BigDecimal fundoReserva;

    @Column(nullable = true)
    private BigDecimal taxaMensal;

    @Column(nullable = true, length = 255)
    private String logoUrl;

    // Construtores
    public Configuracoes() {
    }

    public Configuracoes(String nomeCondominio, String endereco, String cidade, String estado, String sindico, BigDecimal fundoReserva, BigDecimal taxaMensal, String logoUrl) {
        this.nomeCondominio = nomeCondominio;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.sindico = sindico;
        this.fundoReserva = fundoReserva;
        this.taxaMensal = taxaMensal;
        this.logoUrl = logoUrl;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCondominio() {
        return nomeCondominio;
    }

    public void setNomeCondominio(String nomeCondominio) {
        this.nomeCondominio = nomeCondominio;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSindico() {
        return sindico;
    }

    public void setSindico(String sindico) {
        this.sindico = sindico;
    }

    public BigDecimal getFundoReserva() {
        return fundoReserva;
    }

    public void setFundoReserva(BigDecimal fundoReserva) {
        this.fundoReserva = fundoReserva;
    }

    public BigDecimal getTaxaMensal() {
        return taxaMensal;
    }

    public void setTaxaMensal(BigDecimal taxaMensal) {
        this.taxaMensal = taxaMensal;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}