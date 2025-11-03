package manyWorker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import manyWorker.entity.Categoria;
import manyWorker.repository.CategoriaRepository;
import manyWorker.repository.TareaRepository;

@Service
public class CategoriaService {
	@Autowired
    private CategoriaRepository categoriaRepo;

    @Autowired
    private TareaRepository tareaRepo;

    public List<Categoria> listar() {
        return categoriaRepo.findAll();
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepo.save(categoria);
    }

    public Categoria actualizar(String id, Categoria datos) {
        Categoria cat = categoriaRepo.findById(id).orElseThrow();
        cat.setTitulo(datos.getTitulo());
        cat.setLeyesAplicables(datos.getLeyesAplicables());
        return categoriaRepo.save(cat);
    }
    
    public void eliminar(String id) {
    	// Verificamos si hay tareas asociadas a la categoría
        boolean tieneTareas = tareaRepo.existsByCategoria_Id(id);
        if (tieneTareas) {
        	// Si existen, lanzamos una excepción para impedir la eliminación
            throw new IllegalStateException("No se puede eliminar una categoría asociada a tareas.");
        }
        // Si no tiene tareas, se elimina con normalidad
        categoriaRepo.deleteById(id);
    }
}
