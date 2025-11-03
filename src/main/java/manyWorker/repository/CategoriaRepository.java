package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import manyWorker.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {
}