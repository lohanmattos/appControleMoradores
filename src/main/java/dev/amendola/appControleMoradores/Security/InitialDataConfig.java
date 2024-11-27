package dev.amendola.appControleMoradores.Security;

import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Repository.PerfilRepository;
import dev.amendola.appControleMoradores.Repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Configuration
public class InitialDataConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Método executado após a inicialização do sistema.
     */
    @PostConstruct
    @Transactional
    public void init() {
        // Criação dos Perfis
        Perfil adminPerfil = criarPerfilSeNaoExistir("ROLE_ADMIN");
        Perfil userPerfil = criarPerfilSeNaoExistir("ROLE_USER");
        Perfil sindicoPerfil = criarPerfilSeNaoExistir("ROLE_SINDICO");


        // Criação do Usuário Admin
        criarUsuarioSeNaoExistir("admin@example.com", "admin123", true, adminPerfil);

        // Criação do Usuário Padrão
        criarUsuarioSeNaoExistir("user@example.com", "user123", true, userPerfil);
        
        // Criação do Usuário Síndico
        criarUsuarioSeNaoExistir("sindico@example.com", "sindico123", true, sindicoPerfil);
    }

    /**
     * Cria um perfil no banco de dados se ele não existir.
     * 
     * @param nome Nome do perfil
     * @param descricao Descrição do perfil
     * @return Perfil criado ou existente
     */
    private Perfil criarPerfilSeNaoExistir(String descricao) {
        return perfilRepository.findByDescricao(descricao)
                .orElseGet(() -> {
                    Perfil perfil = new Perfil();
                    perfil.setDescricao(descricao);
                    return perfilRepository.save(perfil);
                });
    }

    /**
     * Cria um usuário no banco de dados se ele não existir.
     * 
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @param ativo Indica se o usuário está ativo
     * @param perfis Perfis do usuário
     */
    private void criarUsuarioSeNaoExistir(String email, String senha, boolean ativo, Perfil... perfis) {
        if (usuarioRepository.findByEmail(email) == null) {
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(passwordEncoder.encode(senha));
            usuario.setAtivo(ativo);
            usuario.setPerfis(Arrays.asList(perfis));
            usuarioRepository.save(usuario);
        }
    }
}
