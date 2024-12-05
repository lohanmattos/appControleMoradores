package dev.amendola.appControleMoradores.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class AgendamentoAreaComum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dataFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Responsavel responsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_comum_id", nullable = false)
    private AreaComum areaComum;

    @Column(length = 255)
    private String observacao;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public AreaComum getAreaComum() {
        return areaComum;
    }

    public void setAreaComum(AreaComum areaComum) {
        this.areaComum = areaComum;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
