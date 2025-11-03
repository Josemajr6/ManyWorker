package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import manyWorker.entity.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, String> {
	boolean existsByCategoria_Id(String id);
}
