package dev.amendola.appControleMoradores.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InicioControler {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Página Inicial");
        model.addAttribute("descricao", "Sistema inteligente para gerenciar condominios. ");
        return "redirect:/dashFinanceiro";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuário ou senha inválidos. Tente novamente.");
        }
        if (logout != null) {
            model.addAttribute("message", "Você saiu do sistema com sucesso.");
        }
        return "login";
    }
    
    @GetMapping("/logout")
	String logout() {
		return "login";
	}
    
    @GetMapping("/cadastro")
    public String NovoUsuario(Model model) {
        return "cadastro";
    }
}
