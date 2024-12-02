package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.UsuarioResponsavel;
import dev.amendola.appControleMoradores.Repository.PerfilRepository;
import dev.amendola.appControleMoradores.Repository.UsuarioResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioResponsavelService {

    @Autowired
    private UsuarioResponsavelRepository responsavelRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PerfilRepository perfilRepository;

    public List<UsuarioResponsavel> listarTodos() {
        return responsavelRepository.findAll();
    }

    public UsuarioResponsavel buscarPorId(Long id) {
        return responsavelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário Responsável não encontrado."));
    }

    public void salvar(UsuarioResponsavel responsavel) {
    	String senhaCriptografada = passwordEncoder.encode(responsavel.getUsuario().getSenha());
    	responsavel.getUsuario().setSenha(senhaCriptografada);
    	 // Busca o perfil padrão no banco de dados
        Perfil perfilPadrao = perfilRepository.findByRole("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Perfil padrão 'ROLE_USER' não encontrado."));
        // Adiciona o perfil padrão ao usuário
        if (responsavel.getUsuario().getPerfis() == null || responsavel.getUsuario().getPerfis().isEmpty()) {
        	responsavel.getUsuario().setPerfis(List.of(perfilPadrao));
        }
        
     // Adiciona o perfil padrão ao usuário
        if (responsavel.getUsuario().isAtivo() == false) {
        	responsavel.getUsuario().setAtivo(true);
    }
        responsavelRepository.save(responsavel);
    }

    public void excluir(Long id) {
        if (responsavelRepository.existsById(id)) {
            responsavelRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário Responsável não encontrado.");
        }
    }
}
