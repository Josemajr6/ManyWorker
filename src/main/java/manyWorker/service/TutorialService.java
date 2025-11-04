package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Tutorial;
import manyWorker.repository.TutorialRepository;

@Service
public class TutorialService {

	// Creamos una instancia de TutorialRepository para llamar a los métodos
	@Autowired
	private TutorialRepository tutorialRepository;
	
	// Método que devuelve una lista con todos los tutoriales
	public List<Tutorial> findAll() {
		return this.tutorialRepository.findAll();
	}
	
	// Método que devuelve un tutorial específico por ID
	public Optional<Tutorial> findById(int id) {
		return this.tutorialRepository.findById(id);
	}

	// Método para crear un nuevo tutorial
	public Tutorial save(Tutorial tutorial) {
		return this.tutorialRepository.save(tutorial);
	}

	// Método para actualizar un tutorial
	public Tutorial update(int idTutorial, Tutorial tutorialNuevosDatos) {
		Optional<Tutorial> oTutorial = findById(idTutorial);

		if (oTutorial.isPresent()) {
			Tutorial tutorialAntiguo = oTutorial.get();

			tutorialAntiguo.setTitulo(tutorialNuevosDatos.getTitulo());
			tutorialAntiguo.setResumen(tutorialNuevosDatos.getResumen());
			tutorialAntiguo.setImagenes(tutorialNuevosDatos.getImagenes());
			tutorialAntiguo.setTexto(tutorialNuevosDatos.getTexto());

			return save(tutorialAntiguo);
		}

		return null;
	}

	// Método para eliminar un tutorial
	public void delete(int id) {
		this.tutorialRepository.deleteById(id);
	}

}