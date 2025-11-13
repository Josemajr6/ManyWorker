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

    @Autowired
    private ActorRepository actorRepository;

    public Optional<Mensaje> findById(int id) {
        return mensajeRepository.findById(id);
    }

    public List<Mensaje> findAll() {
        return mensajeRepository.findAll();
    }

    public Mensaje save(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public void delete(int id) {
        mensajeRepository.deleteById(id);
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
    
    // Enviar mensaje de broadcast (solo admin)
    public List<Mensaje> enviarBroadcast(int idRemitente, String asunto, String cuerpo) {

        Actor remitente = actorRepository.findById(idRemitente)
                .orElseThrow(() -> new RuntimeException("Remitente no encontrado"));

        // Verificar que el remitente sea administrador
        // if (remitente.getAuthority() == null || !remitente.getAuthority().equalsIgnoreCase("admin")) {
        //    throw new RuntimeException("Solo los administradores pueden enviar mensajes broadcast");
        // }

        // Obtener todos los actores
        List<Actor> todosActores = actorRepository.findAll();

        // Crear lista para los mensajes a enviar
        List<Mensaje> mensajes = new java.util.ArrayList<>();

        // Recorrer los actores y crear un mensaje para cada uno
        for (Actor destinatario : todosActores) {
            
        	// Evitar enviarse a sí mismo
            if (destinatario.getId() != remitente.getId()) {
                Mensaje nuevo = new Mensaje(remitente, destinatario, new java.util.Date(), asunto, cuerpo);
                mensajes.add(nuevo);
            }
        }

        // Guardar todos los mensajes en la base de datos
        mensajeRepository.saveAll(mensajes);

        return mensajes;
    }
}
