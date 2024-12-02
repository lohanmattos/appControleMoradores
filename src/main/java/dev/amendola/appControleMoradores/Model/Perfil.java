package dev.amendola.appControleMoradores.Model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String role;
    
    @Column(nullable = false, unique = true)
    private String descricao;

    // Getters e Setters
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

    @Override
    public String toString() {
        return descricao; // Retorna apenas a descrição do perfil
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Perfil other = (Perfil) obj;
		return Objects.equals(role, other.role);
	}

    @Override
	public int hashCode() {
		return Objects.hash(role);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    
}
