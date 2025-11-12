package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Tarea;
import manyWorker.repository.TareaRepository;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepo;

    /**
     * Obtiene todas las tareas registradas.
     */
    public List<Tarea> findAll() {
        return tareaRepo.findAll();
    }

    /**
     * Busca una tarea por su ID.
     */
    public Optional<Tarea> findById(String id) {
        return tareaRepo.findById(id);
    }

    /**
     * Guarda una nueva tarea en la base de datos.
     */
    public Tarea save(Tarea tarea) {
        return tareaRepo.save(tarea);
    }

    /**
     * Actualiza una tarea existente.
     */
    public Tarea update(String id, Tarea datos) {
        Optional<Tarea> optional = tareaRepo.findById(id);
        if (optional.isPresent()) {
            Tarea tarea = optional.get();
            tarea.setDescripcion(datos.getDescripcion());
            tarea.setDireccion(datos.getDireccion());
            tarea.setPrecioMax(datos.getPrecioMax());
            tarea.setFechaFin(datos.getFechaFin());
            tarea.setCategoria(datos.getCategoria());
            return tareaRepo.save(tarea);
        }
        return null;
    }

    /**
     * Elimina una tarea según su ID.
     */
    public void delete(String id) {
        tareaRepo.deleteById(id);
    }
}

