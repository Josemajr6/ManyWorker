package manyWorker.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import manyWorker.entity.Actor;
import manyWorker.entity.Mensaje;
import manyWorker.entity.Roles;
import manyWorker.repository.ActorRepository;
import manyWorker.repository.MensajeRepository;

@Service
public class MensajeService {

	@Autowired
	private MensajeRepository mensajeRepository;

	@Autowired
	private ActorRepository actorRepository;

	public Optional<Mensaje> findById(int id) {
		Optional<Mensaje> mensaje = mensajeRepository.findById(id);

		if (mensaje.isEmpty())
			return Optional.empty();

		Actor actorAutenticado = (Actor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Mensaje m = mensaje.get();

		boolean esAdmin = actorAutenticado.getRol() == Roles.ADMINISTRADOR;

		if (!esAdmin && m.getRemitente().getId() != (actorAutenticado.getId())
				&& m.getDestinatario().getId() != (actorAutenticado.getId())) {

			throw new AccessDeniedException("No tienes permiso para acceder a este mensaje");
		}

		return mensaje;
	}

	public List<Mensaje> findAll() {
		return mensajeRepository.findAll();
	}

	public Mensaje save(Mensaje mensaje) {
		return mensajeRepository.save(mensaje);
	}

	public void delete(int id) {
		Mensaje m = mensajeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

		Actor actorAutenticado = (Actor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		boolean esAdmin = actorAutenticado.getRol() == Roles.ADMINISTRADOR;

		if (!esAdmin && m.getRemitente().getId() != (actorAutenticado.getId())
				&& m.getDestinatario().getId() != (actorAutenticado.getId())) {

			throw new AccessDeniedException("No tienes permiso para eliminar este mensaje");
		}

		mensajeRepository.delete(m);
	}

	public boolean existsById(int id) {
		return mensajeRepository.existsById(id);
	}

	// Enviar un mensaje entre actores
	public Mensaje enviarMensaje(int idRemitente, int idDestinatario, String asunto, String cuerpo) {
		Optional<Actor> oRemitente = actorRepository.findById(idRemitente);
		Optional<Actor> oDestinatario = actorRepository.findById(idDestinatario);

		if (!oRemitente.isPresent() || !oDestinatario.isPresent()) {
			throw new RuntimeException("Remitente o destinatario no encontrados");
		}

		Actor remitente = oRemitente.get();
		Actor destinatario = oDestinatario.get();

		Mensaje mensaje = new Mensaje(remitente, destinatario, new Date(), asunto, cuerpo);
		return mensajeRepository.save(mensaje);
	}

	// Enviar mensaje de broadcast
	public List<Mensaje> enviarBroadcast(int idRemitente, String asunto, String cuerpo) {
		Actor remitente = actorRepository.findById(idRemitente)
				.orElseThrow(() -> new RuntimeException("Remitente no encontrado"));

		List<Actor> todosActores = actorRepository.findAll();
		List<Mensaje> mensajes = new java.util.ArrayList<>();

		for (Actor destinatario : todosActores) {
			if (destinatario.getId() != remitente.getId()) {
				Mensaje nuevo = new Mensaje(remitente, destinatario, new Date(), asunto, cuerpo);
				mensajes.add(nuevo);
			}
		}

		mensajeRepository.saveAll(mensajes);
		return mensajes;
	}
}