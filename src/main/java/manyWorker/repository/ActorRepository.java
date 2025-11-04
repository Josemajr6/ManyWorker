package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import manyWorker.entity.Actor;

public interface ActorRepository extends JpaRepository<Actor, Integer>{

}
