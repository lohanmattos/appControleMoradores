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
                .requestMatchers("/static/**").permitAll() // Permite acesso público aos arquivos estáticos
                .anyRequest().authenticated() // Requer autenticação para as demais requisições
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/", true) // Redireciona para a página principal após login
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf().disable();
            ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

}
