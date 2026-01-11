package manyWorker.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>{
	@Query("SELECT a FROM Actor a WHERE a.username=?1")
	Optional<Actor> findByUsername(String username);
}
