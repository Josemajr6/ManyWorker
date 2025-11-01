package manyWorker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
	
	List<Mensaje> findByRemitenteId(int remitenteId);

    List<Mensaje> findByDestinatarioId(int destinatarioId);
}