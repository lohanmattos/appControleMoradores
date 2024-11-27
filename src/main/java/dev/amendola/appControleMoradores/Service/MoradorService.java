package dev.amendola.appControleMoradores.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.amendola.appControleMoradores.Model.Morador;
import dev.amendola.appControleMoradores.Repository.MoradorRepository;

@Service
public class MoradorService {

    @Autowired
    private MoradorRepository moradorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Morador> buscarTodos() {
        return moradorRepository.findAll();
    }

    public void salvar(Morador morador) {
        // Criptografa a senha do usuário
        if (morador.getUsuario() != null) {
            String senhaCriptografada = passwordEncoder.encode(morador.getUsuario().getSenha());
            morador.getUsuario().setSenha(senhaCriptografada);
        }

        // Salva o morador com o usuário
        moradorRepository.save(morador);
    }

    public Morador buscarPorCpf(String cpf) {
        return moradorRepository.findByCpf(cpf);
    }

    public void excluir(Long id) {
        moradorRepository.deleteById(id);
    }
}
