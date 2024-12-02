package dev.amendola.appControleMoradores.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.amendola.appControleMoradores.Model.UsuarioResponsavel;
import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Repository.UsuarioResponsavelRepository;
import dev.amendola.appControleMoradores.Repository.PerfilRepository;

@Service
public class UsuarioResponsavelService {

    @Autowired
    private UsuarioResponsavelRepository moradorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PerfilRepository perfilRepository;

    public List<UsuarioResponsavel> buscarTodos() {
        return moradorRepository.findAll();
    }

    public void salvar(UsuarioResponsavel morador) {
        if (morador.getUsuario() != null) {
            // Criptografa a senha do usuário
            String senhaCriptografada = passwordEncoder.encode(morador.getUsuario().getSenha());
            morador.getUsuario().setSenha(senhaCriptografada);

            // Busca o perfil padrão no banco de dados
            Perfil perfilPadrao = perfilRepository.findByRole("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Perfil padrão 'ROLE_USER' não encontrado."));

            // Adiciona o perfil padrão ao usuário
            if (morador.getUsuario().getPerfis() == null || morador.getUsuario().getPerfis().isEmpty()) {
                morador.getUsuario().setPerfis(List.of(perfilPadrao));
            }
            
         // Adiciona o perfil padrão ao usuário
            if (morador.getUsuario().isAtivo() == false) {
                morador.getUsuario().setAtivo(true);
        }
        }
        // Salva o morador com o usuário
        moradorRepository.save(morador);
    }

    public UsuarioResponsavel buscarPorCpf(String cpf) {
        return moradorRepository.findByCpf(cpf);
    }

    public void excluir(Long id) {
        moradorRepository.deleteById(id);
    }
    
    public UsuarioResponsavel buscarPorId(Long id) {
        return moradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Morador não encontrado."));
    }

	public Object listarTodos() {
		// TODO Stub de método gerado automaticamente
		return moradorRepository.findAll();
	}
}
