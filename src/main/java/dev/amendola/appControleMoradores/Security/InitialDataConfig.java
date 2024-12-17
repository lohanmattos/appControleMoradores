package dev.amendola.appControleMoradores.Security;

import dev.amendola.appControleMoradores.Model.ConfigCondominio;
import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Repository.ConfigCondominioRepository;
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
    private ConfigCondominioRepository configCondominioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Método executado após a inicialização do sistema.
     */
    @PostConstruct
    @Transactional
    public void init() {
        // Criação dos Perfis
        Perfil adminPerfil = criarPerfilSeNaoExistir("ADMIN", "ADMINISTRADOR");
        //Perfil usuarioPerfil = criarPerfilSeNaoExistir("USUARIO", "USUARIO");
        Perfil sindicoPerfil = criarPerfilSeNaoExistir("SINDICO", "SINDICO");

        // Criação do Usuário Admin
        criarUsuarioSeNaoExistir("admin@example.com", "admin123", true, adminPerfil);
   
     // Criação do Usuário Admin
        criarUsuarioSeNaoExistir("sindico@example.com", "sindico123", true, sindicoPerfil);

        // Criação da Configuração do Condomínio
        criarConfigCondominioSeNaoExistir("Condomínio Central", "Rua Exemplo, 123", "Cidade Exemplo", "EX");
    }

    /**
     * Cria um perfil no banco de dados se ele não existir.
     * 
     * @param role Nome do perfil
     * @param descricao Descrição do perfil
     * @return Perfil criado ou existente
     */
    private Perfil criarPerfilSeNaoExistir(String role, String descricao) {
        return perfilRepository.findByRole(role)
                .orElseGet(() -> {
                    Perfil perfil = new Perfil();
                    perfil.setRole(role);
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

    /**
     * Cria a configuração do condomínio no banco de dados se ela não existir.
     *
     * @param nome Nome do condomínio
     * @param endereco Endereço do condomínio
     * @param cidade Cidade do condomínio
     * @param estado Estado do condomínio
     * @param sindico Nome do síndico do condomínio
     */
    private void criarConfigCondominioSeNaoExistir(String nome, String endereco, String cidade, String estado) {
        if (configCondominioRepository.count() == 0) { // Verifica se a configuração já existe
            ConfigCondominio configCondominio = new ConfigCondominio();
            configCondominio.setNomeCondominio(nome);
            configCondominio.setEndereco(endereco);
            configCondominio.setCidade(cidade);
            configCondominio.setEstado(estado);
            configCondominioRepository.save(configCondominio);
        }
    }
    
}
