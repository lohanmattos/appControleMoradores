package dev.amendola.appControleMoradores.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;


import dev.amendola.appControleMoradores.Model.Perfil;
import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	
	 @Autowired
	    private UsuarioRepository repository;

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
	   	    
}