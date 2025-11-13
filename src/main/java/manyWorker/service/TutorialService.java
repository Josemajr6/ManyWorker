package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Tutorial;
import manyWorker.entity.Trabajador;
import manyWorker.repository.TutorialRepository;

@Service
public class TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;
    
    public List<Tutorial> findAll() {
        return this.tutorialRepository.findAll();
    }
    
    public Optional<Tutorial> findById(int id) {
        return this.tutorialRepository.findById(id);
    }

    public Tutorial save(Tutorial tutorial) {
        return this.tutorialRepository.save(tutorial);
    }

    public Tutorial update(int idTutorial, Tutorial tutorialNuevosDatos) {
        Optional<Tutorial> oTutorial = findById(idTutorial);

        if (oTutorial.isPresent()) {
            Tutorial tutorialAntiguo = oTutorial.get();

            tutorialAntiguo.setTitulo(tutorialNuevosDatos.getTitulo());
            tutorialAntiguo.setResumen(tutorialNuevosDatos.getResumen());
            tutorialAntiguo.setImagenes(tutorialNuevosDatos.getImagenes());
            tutorialAntiguo.setTexto(tutorialNuevosDatos.getTexto());
            tutorialAntiguo.setAutor(tutorialNuevosDatos.getAutor());

            return save(tutorialAntiguo);
        }
        return null;
    }

    public void delete(int id) {
        this.tutorialRepository.deleteById(id);
    }

    public List<Tutorial> findByAutor(Trabajador autor) {
        return this.tutorialRepository.findByAutor(autor);
    }
    
    public List<Tutorial> findByAutorId(int autorId) {
        return this.tutorialRepository.findByAutorId(autorId);
    }
    
    public boolean existsById(int id) {
        return this.tutorialRepository.existsById(id);
    }
}