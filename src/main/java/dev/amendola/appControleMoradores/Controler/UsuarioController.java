package dev.amendola.appControleMoradores.Controler;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.amendola.appControleMoradores.Model.Usuario;
import dev.amendola.appControleMoradores.Service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/perfil")
    public String userProfile(Model model, Principal principal) {
        Usuario user = usuarioService.findByEmail(principal.getName());
        model.addAttribute("user", user); // Adiciona o objeto usuário ao modelo
        return "usuario/usuario-perfil";
    }

    @PostMapping("/perfil")
    public String updateUserProfile(Usuario user, Principal principal, Model model) {
        try {
            // Obtém o usuário atual pelo email
            Usuario existingUser = usuarioService.findByEmail(principal.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setSenha(passwordEncoder.encode(user.getSenha())); // Atualize com codificação da senha, se necessário
            usuarioService.updateUser(existingUser);

            model.addAttribute("success", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar o perfil.");
        }

        return "redirect:/usuario/perfil";
    }
    
    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios); // Adiciona a lista de usuários ao modelo
        return "usuario/lista-usuarios";
    }
}
