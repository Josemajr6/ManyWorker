package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import manyWorker.entity.Actor;
import manyWorker.entity.PerfilSocial;
import manyWorker.repository.PerfilSocialRepository;

@Service
public class PerfilSocialService {

	@Autowired
	private PerfilSocialRepository perfilSocialRepository;

	public Optional<PerfilSocial> findById(int id) {
		return this.perfilSocialRepository.findById(id);
	}

	public List<PerfilSocial> findAll() {
		return this.perfilSocialRepository.findAll();
	}

	public PerfilSocial save(PerfilSocial perfilSocial) {
		return this.perfilSocialRepository.save(perfilSocial);
	}

	public PerfilSocial update(int idPerfilSocial, PerfilSocial perfilSocial) {
		Optional<PerfilSocial> oPerfilSocial = findById(idPerfilSocial);

	    if (oPerfilSocial.isEmpty()) {
	        return null;
	    }

	    PerfilSocial ps = oPerfilSocial.get();

	    Actor actorAutenticado = (Actor) SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getPrincipal();

	    if (!actorAutenticado.getNumeroPerfiles().contains(ps)) {
	        throw new AccessDeniedException("No tienes permiso para modificar este perfil social");
	    }

	    ps.setApodo(perfilSocial.getApodo());
	    ps.setNombreRedSocial(perfilSocial.getNombreRedSocial());
	    ps.setEnlace(perfilSocial.getEnlace());

	    return save(ps);
	}

	public void delete(int id) {
		Optional<PerfilSocial> oPerfilSocial = findById(id);

	    if (oPerfilSocial.isEmpty()) {
	        return;
	    }

	    PerfilSocial ps = oPerfilSocial.get();

	    Actor actorAutenticado = (Actor) SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getPrincipal();

	    if (!actorAutenticado.getNumeroPerfiles().contains(ps)) {
	        throw new AccessDeniedException("No tienes permiso para eliminar este perfil social");
	    }

	    perfilSocialRepository.delete(ps);
	}
	
	public boolean existsById(int id) {
	    return this.perfilSocialRepository.existsById(id);
	}
}