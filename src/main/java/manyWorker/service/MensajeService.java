package manyWorker.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Actor;
import manyWorker.entity.Mensaje;
import manyWorker.repository.ActorRepository;
import manyWorker.repository.MensajeRepository;

@Service
public class MensajeService {

	@Autowired
	private MensajeRepository mensajeRepository;
	private ActorRepository actorRepository;
	
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
	
	// Enviar un mensaje entre actores
    public Mensaje enviarMensaje(int IdRemitente, int IdDestinatario, String asunto, String cuerpo) {
    	
    	Optional<Actor> oRemitente = actorRepository.findById(IdRemitente);
    	Optional<Actor> oDestinatario = actorRepository.findById(IdDestinatario);
    	Actor remitente = null;
    	Actor destinatario = null;
    	
        		if (oRemitente.isPresent() && oDestinatario.isPresent()) {
        			remitente = oRemitente.get();
        			destinatario = oDestinatario.get();
        		}
        		else throw new RuntimeException("Remitente, Destinatario o ambos no existen");

        Mensaje mensaje = new Mensaje(remitente, destinatario, new Date(), asunto, cuerpo);
        return mensajeRepository.save(mensaje);
    }

    // Obtener mensajes enviados por un actor
    public List<Mensaje> obtenerMensajesEnviados(int id) {
        return mensajeRepository.findByRemitenteId(id);
    }

    // Obtener mensajes recibidos por un actor
    public List<Mensaje> obtenerMensajesRecibidos(int id) {
        return mensajeRepository.findByDestinatarioId(id);
    }
}