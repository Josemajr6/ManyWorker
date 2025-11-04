package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manyWorker.entity.Tutorial;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Integer> {

}
