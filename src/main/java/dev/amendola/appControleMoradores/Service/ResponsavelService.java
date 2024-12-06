package dev.amendola.appControleMoradores.Service;
import dev.amendola.appControleMoradores.Model.Responsavel;
import dev.amendola.appControleMoradores.Repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;

    public List<Responsavel> listarTodos() {
        return responsavelRepository.findAll();
    }
    
    public Responsavel buscarPorId(Long id) {
        return responsavelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário Responsável não encontrado."));
    }
    
    public Responsavel salvarResponsavel(Responsavel responsavel) {
		return responsavelRepository.save(responsavel);
		
	}

    public Responsavel findByUsuarioId(String usuarioId) {
        return responsavelRepository.findByUsuarioId(usuarioId);
    }
    
    public List<Responsavel> filtrarResponsaveisSindico(List<Responsavel> responsaveis) {
        return responsaveis.stream()
                .filter(responsavel -> responsavel.getUsuario() != null) // Verifica se há um usuário associado
                .filter(responsavel -> responsavel.getUsuario().getPerfis().stream()
                        .anyMatch(perfil -> "SINDICO".equalsIgnoreCase(perfil.getRole()))) // Verifica o perfil de síndico
                .collect(Collectors.toList());
    }
    
}
