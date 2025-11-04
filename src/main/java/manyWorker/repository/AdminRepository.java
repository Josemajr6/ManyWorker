package manyWorker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import manyWorker.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{

}
