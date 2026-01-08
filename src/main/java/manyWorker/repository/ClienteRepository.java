package manyWorker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Cliente;
import manyWorker.entity.Trabajador;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	Optional<Cliente> findByUsername(String username);
}
