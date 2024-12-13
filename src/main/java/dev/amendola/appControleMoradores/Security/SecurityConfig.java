package dev.amendola.appControleMoradores.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests((authorize) -> authorize
	            .requestMatchers("/dist/**", "/plugins/**").permitAll() // Permite recursos públicos
	            .requestMatchers("/login", "/logout").permitAll() // Permite acesso à página de login/logout
	            .requestMatchers("/categorias", "/configuracoes", "/responsaveis","/usuarios/listar").hasAuthority("SINDICO")
	            .anyRequest().authenticated() // Exige autenticação para as outras requisições
	        )
	        .formLogin(form -> form
	            .loginPage("/login") // Página de login customizada
	            .failureUrl("/login?error=true") // URL para erros de login
	            .defaultSuccessUrl("/", true) // Redireciona após login bem-sucedido
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout=true")
	            .deleteCookies("JSESSIONID")
	            .invalidateHttpSession(true)
	            .permitAll()
	        );

	    // Remove o HTTP Basic
	    // .httpBasic(Customizer.withDefaults()); 

	    return http.build();
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

}
