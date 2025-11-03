package manyWorker.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import manyWorker.entity.Tarea;
import manyWorker.repository.TareaRepository;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
	 @Autowired
	    private TareaRepository tareaRepo;

	    @GetMapping
	    public List<Tarea> listar() {
	        return tareaRepo.findAll();
	    }

	    @PostMapping
	    public Tarea crear(@RequestBody Tarea tarea) {
	        return tareaRepo.save(tarea);
	    }

	    @PutMapping("/{id}")
	    public Tarea actualizar(@PathVariable String id, @RequestBody Tarea datos) {
	        Tarea tarea = tareaRepo.findById(id).orElseThrow();
	        tarea.setDescripcion(datos.getDescripcion());
	        tarea.setDireccion(datos.getDireccion());
	        tarea.setPrecioMax(datos.getPrecioMax());
	        tarea.setFechaFin(datos.getFechaFin());
	        tarea.setCategoria(datos.getCategoria());
	        return tareaRepo.save(tarea);
	    }

	    @DeleteMapping("/{id}")
	    public void eliminar(@PathVariable String id) {
	        tareaRepo.deleteById(id);
	    }
}
