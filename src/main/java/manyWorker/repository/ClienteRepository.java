package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import manyWorker.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
