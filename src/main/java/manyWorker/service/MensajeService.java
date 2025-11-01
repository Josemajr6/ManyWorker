package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Mensaje;
import manyWorker.repository.MensajeRepository;

@Service
public class MensajeService {

	@Autowired
	private MensajeRepository mensajeRepository;
	
	public Optional<Mensaje> findById(int id) {
		return this.mensajeRepository.findById(id);
	}
	
	public List<Mensaje> findAll() {
		return this.mensajeRepository.findAll();
	}
	
	public Mensaje save(Mensaje mensaje) {
		return this.mensajeRepository.save(mensaje);
	}
	
	public void delete(int id) {
		this.mensajeRepository.deleteById(id);
	}
}