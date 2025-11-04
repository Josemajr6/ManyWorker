package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	// TODO; Solo el usuario propietario puede realizar esta accion
	public PerfilSocial update(int idPerfilSocial, PerfilSocial perfilSocial) {
		Optional<PerfilSocial> oPerfilSocial = findById(idPerfilSocial);
		if (oPerfilSocial.isPresent()) {
			PerfilSocial ps = oPerfilSocial.get();
			ps.setApodo(perfilSocial.getApodo());
			ps.setNombreRedSocial(perfilSocial.getNombreRedSocial());
			ps.setEnlace(perfilSocial.getEnlace());
			return save(ps);
		}
		return null;
	}

	// TODO; Solo el usuario propietario puede realizar esta accion
	// TODO: Posteriormente se van a a anonimizar los datos en vez de eliminar.
	public void delete(int id) {
		this.perfilSocialRepository.deleteById(id);
	}
	
}
