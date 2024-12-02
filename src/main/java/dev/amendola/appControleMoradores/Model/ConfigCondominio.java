package dev.amendola.appControleMoradores.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "ConfigCondominio")
public class ConfigCondominio {

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

    // Construtores
    public ConfigCondominio() {
    }

    public ConfigCondominio(String nomeCondominio, String endereco, String cidade, String estado, String sindico,  String logoUrl) {
        this.nomeCondominio = nomeCondominio;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.sindico = sindico;
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

}