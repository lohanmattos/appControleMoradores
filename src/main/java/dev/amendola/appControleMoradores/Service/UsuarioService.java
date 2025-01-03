package dev.amendola.appControleMoradores.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;


import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.Responsavel;
import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	
	 @Autowired
	 private UsuarioRepository repository;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 
	 @Autowired
	 private ResponsavelService responsavelService;

	    /**
	     * Busca o usuário pelo email no banco de dados.
	     * 
	     * @param email o email do usuário
	     * @return o objeto Usuario encontrado
	     */
	    @Transactional(readOnly = true)
	    public Usuario buscarPorEmail(String username) {
	        Usuario usuario = repository.findByEmail(username);
	        if (usuario == null) {
	            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
	        }
	        return usuario;
	    }

	    /**
	     * Implementação do método para carregar um usuário por nome de usuário (email).
	     * 
	     * @param username o email do usuário
	     * @return os detalhes do usuário para autenticação no Spring Security
	     */
	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Usuario usuario = buscarPorEmail(username);

	        // Constrói o UserDetails do Spring Security
	        return User.builder()
	                .username(usuario.getEmail())
	                .password(usuario.getSenha())
	                .authorities(getAuthorities(usuario.getPerfis())) // Perfis convertidos em autoridades
	                .accountLocked(!usuario.isAtivo()) // Define se o usuário está ativo
	                .build();
	    }


	    /**
	     * Transforma a lista de perfis do usuário em uma lista de autoridades para o Spring Security.
	     *
	     * @param perfis a lista de perfis do usuário
	     * @return uma lista de autoridades (SimpleGrantedAuthority) representando os perfis
	     */
	    private List<SimpleGrantedAuthority> getAuthorities(List<Perfil> perfis) {
	        return perfis.stream()
	                .map(perfil -> new SimpleGrantedAuthority(perfil.getDescricao())) // Converte para SimpleGrantedAuthority
	                .toList(); // Converte para lista
	    }

		public Usuario findByEmail(String username) {
	        Usuario usuario = repository.findByEmail(username);
	        
			return usuario;
		}
		
	    public void updateUser(Usuario user) {
	    	repository.save(user); // Atualiza o usuário
	    }

	    public List<Usuario> findAll() {
	        return repository.findAll(); // Busca todos os usuários
	    }

		
	    public Usuario cadastrarUsuario(Usuario usuario) {
	        // Busca o usuário existente no banco de dados, caso ele já esteja cadastrado
	        Usuario usuarioExistente = repository.findById(usuario.getId()).orElse(null);

	        if (usuarioExistente != null) {
	            // Se a senha não foi enviada, mantém a senha existente
	            if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
	                usuario.setSenha(usuarioExistente.getSenha());
	            } else {
	                // Criptografa a nova senha caso tenha sido enviada
	                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
	            }
	        } else {
	            // Se o usuário não existe, criptografa a senha obrigatoriamente
	            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
	        }

	        // Salva o usuário no repositório
	        Usuario usuarioSalvo = repository.save(usuario);

	        return usuarioSalvo;
	    }


	   	    
		public Usuario buscarUsuarioPorId(String id) {
		    return repository.findById(id)
		        .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
		}

		public void excluirUsuario(String id) {
			// TODO Auto-generated method stub
			
			Usuario user = this.buscarUsuarioPorId(id);
			repository.delete(user);
		}

		public Optional<Usuario> OptbuscarUsuarioPorId(String id) {
		    return repository.findById(id);
		}
}
