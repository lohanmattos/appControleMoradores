package dev.amendola.appControleMoradores.Service;
import dev.amendola.appControleMoradores.Model.Responsavel;
import dev.amendola.appControleMoradores.Repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
    
}
