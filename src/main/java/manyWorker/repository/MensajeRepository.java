package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
	
}