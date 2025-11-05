package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import manyWorker.entity.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
	
}