package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import manyWorker.entity.Roles;
import manyWorker.entity.Trabajador;
import manyWorker.repository.TrabajadorRepository;

@Service
public class TrabajadorService {

	@Autowired
	private TrabajadorRepository trabajadorRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Optional<Trabajador> findByUsername(String username) {
        return trabajadorRepository.findByUsername(username);
    }
	
	public Optional<Trabajador> findById(int id) {
		return this.trabajadorRepository.findById(id);
	}

	public List<Trabajador> findAll() {
		return this.trabajadorRepository.findAll();
	}

	public Trabajador save(Trabajador trabajador) {
		trabajador.setRol(Roles.TRABAJADOR);
		
		if (trabajador.getPassword() != null) {
            String encodedPass = passwordEncoder.encode(trabajador.getPassword());
            trabajador.setPassword(encodedPass);
        }
		
		return this.trabajadorRepository.save(trabajador);
	}

	// TODO; Solo el usuario propietario puede realizar esta accion
	public Trabajador update(int idTrabajador, Trabajador trabajador) {
		Optional<Trabajador> oTrabajador= findById(idTrabajador);
		if (oTrabajador.isPresent()) {
			Trabajador t = oTrabajador.get();
			t.setNombre(trabajador.getNombre());
			t.setApellido(trabajador.getApellido());
			t.setApellido2(trabajador.getApellido2());
			t.setCorreo(trabajador.getCorreo());
			t.setFoto(trabajador.getFoto());
			t.setTelefono(trabajador.getTelefono());
			t.setDireccion(trabajador.getDireccion());
			return save(t);
		}
		return null;
	}

	// TODO; Solo el usuario propietario puede realizar esta accion
	// TODO: Posteriormente se van a a anonimizar los datos en vez de eliminar.
	public void delete(int id) {
		this.trabajadorRepository.deleteById(id);
	}
	
	public boolean existsById(int id) {
	    return this.trabajadorRepository.existsById(id);
	}
}