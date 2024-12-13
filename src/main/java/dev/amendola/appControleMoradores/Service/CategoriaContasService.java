package dev.amendola.appControleMoradores.Service;

import dev.amendola.appControleMoradores.Model.CategoriaContas;
import dev.amendola.appControleMoradores.Model.TipoCategoria;
import dev.amendola.appControleMoradores.Repository.CategoriaContasRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaContasService {
	
	@Autowired
    private CategoriaContasRepository categoriaContasRepository;
	
	 public List<CategoriaContas> listarPorTipo(TipoCategoria tipo) {
	        return categoriaContasRepository.findByTipo(tipo);
	    }
	 
	 public Page<CategoriaContas> listarCategorias(Pageable pageable) {
	        return categoriaContasRepository.findAll(pageable);
	    }
	 
	 public List<CategoriaContas> listarTodos() {
	        return categoriaContasRepository.findAll();
	    }

	    public CategoriaContas salvar(CategoriaContas categoriaContas) {
	        return categoriaContasRepository.save(categoriaContas);
	    }
	    
	    public void deletar(Long id) {
	    	categoriaContasRepository.deleteById(id);
	    }
	    

}
