package dev.amendola.appControleMoradores.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "imoveis")
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false, length = 100)
    private String endereco;


    @OneToOne
    @JoinColumn(name = "responsavel_id", nullable = true) // Pode ser nulo para imóveis sem responsável
    private Responsavel responsavel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Responsavel getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Responsavel responsavel) {
		this.responsavel = responsavel;
	}

    
}
