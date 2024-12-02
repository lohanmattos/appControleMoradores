package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.Imovel;
import dev.amendola.appControleMoradores.Repository.ImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    public List<Imovel> listarTodos() {
        return imovelRepository.findAll();
    }

    public Imovel buscarPorId(Long id) {
        return imovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
    }

    public Imovel salvar(Imovel imovel) {
        return imovelRepository.save(imovel);
    }

    public Imovel atualizar(Long id, Imovel imovelAtualizado) {
        Imovel imovel = buscarPorId(id);
        imovel.setDescricao(imovelAtualizado.getDescricao());
        imovel.setEndereco(imovelAtualizado.getEndereco());
        imovel.setResponsavel(imovelAtualizado.getResponsavel());
        return imovelRepository.save(imovel);
    }

    public void deletar(Long id) {
        imovelRepository.deleteById(id);
    }
}
