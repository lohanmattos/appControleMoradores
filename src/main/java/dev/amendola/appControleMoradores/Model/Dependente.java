package dev.amendola.appControleMoradores.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "dependentes")
public class Dependente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String parentesco; // Exemplo: Filho, Esposa, etc.

    @ManyToOne
    @JoinColumn(name = "responsavel_id", nullable = false)
    private UsuarioResponsavel responsavel;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public UsuarioResponsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioResponsavel responsavel) {
        this.responsavel = responsavel;
    }

}
