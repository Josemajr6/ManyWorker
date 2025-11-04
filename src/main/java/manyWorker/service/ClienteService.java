package manyWorker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Cliente;
import manyWorker.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public Optional<Cliente> findById(int id) {
		return this.clienteRepository.findById(id);
	}

	public List<Cliente> findAll() {
		return this.clienteRepository.findAll();
	}

	public Cliente save(Cliente cliente) {
		return this.clienteRepository.save(cliente);
	}

	// TODO; Solo el usuario propietario puede realizar esta accion
	public Cliente update(int idCliente, Cliente cliente) {
		Optional<Cliente> oCliente = findById(idCliente);
		if (oCliente.isPresent()) {
			Cliente c = oCliente.get();
			c.setNombre(cliente.getNombre());
			c.setApellido(cliente.getApellido());
			c.setApellido2(cliente.getApellido2());
			c.setCorreo(cliente.getCorreo());
			c.setFoto(cliente.getFoto());
			c.setTelefono(cliente.getTelefono());
			c.setDireccion(cliente.getDireccion());
			return save(c);
		}
		return null;
	}

	// TODO; Solo el usuario propietario puede realizar esta accion
	// TODO: Posteriormente se van a a anonimizar los datos en vez de eliminar.
	public void delete(int id) {
		this.clienteRepository.deleteById(id);
	}
	
}
