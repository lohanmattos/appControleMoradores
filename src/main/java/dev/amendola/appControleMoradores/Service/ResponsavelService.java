package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Model.Responsavel;
import dev.amendola.appControleMoradores.Repository.PerfilRepository;
import dev.amendola.appControleMoradores.Repository.UsuarioRepository;
import dev.amendola.appControleMoradores.Repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PerfilRepository perfilRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Responsavel> listarTodos() {
        return responsavelRepository.findAll();
    }

    public Responsavel buscarPorId(Long id) {
        return responsavelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário Responsável não encontrado."));
    }

    public void salvar(Responsavel responsavel) {
        if (responsavel.getUsuario() != null) {
            Usuario usuarioExistente = null;

            if (responsavel.getUsuario().getId() != null) {
                // Recupera o usuário existente pelo ID
                usuarioExistente = usuarioRepository.findById(responsavel.getUsuario().getId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            }

            if (usuarioExistente != null) {
                // Mantém o e-mail e a senha do usuário existente
                usuarioExistente.setEmail(usuarioExistente.getEmail());
                usuarioExistente.setSenha(usuarioExistente.getSenha());

                // Atualiza apenas o status ativo se foi alterado
                usuarioExistente.setAtivo(responsavel.getUsuario().isAtivo());
                usuarioExistente.setPerfis(usuarioExistente.getPerfis());

                // Relaciona novamente o usuário ao responsável
                responsavel.setUsuario(usuarioExistente);
            } else {
                // Novo usuário: Criptografe a senha e adicione o perfil padrão
                String senhaCriptografada = passwordEncoder.encode(responsavel.getUsuario().getSenha());
                responsavel.getUsuario().setSenha(senhaCriptografada);

                if (responsavel.getUsuario().getPerfis() == null || responsavel.getUsuario().getPerfis().isEmpty()) {
                    Perfil perfilPadrao = perfilRepository.findByRole("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Perfil padrão 'ROLE_USER' não encontrado."));
                    responsavel.getUsuario().setPerfis(new ArrayList<>(List.of(perfilPadrao)));
                }

                // Define o status como ativo por padrão
                responsavel.getUsuario().setAtivo(true);
            }
        }

        // Salva o responsável e o relacionamento com o usuário
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
