package manyWorker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import manyWorker.entity.Cliente;
import manyWorker.entity.Roles;
import manyWorker.entity.Trabajador;
import manyWorker.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Optional<Cliente> findByUsername(String username) {
        return clienteRepository.findByUsername(username);
    }
    
    public Optional<Cliente> findById(int id) {
        return this.clienteRepository.findById(id);
    }

    public List<Cliente> findAll() {
        return this.clienteRepository.findAll();
    }

    public Cliente save(Cliente cliente) {
    	cliente.setRol(Roles.CLIENTE); 
        
        if (cliente.getPassword() != null) {
            String encodedPass = passwordEncoder.encode(cliente.getPassword());
            cliente.setPassword(encodedPass);
        }
    	
        return this.clienteRepository.save(cliente);
    }

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
            c.setNumeroPerfiles(cliente.getNumeroPerfiles());
            return save(c);
        }
        return null;
    }

    public void delete(int id) {
        this.clienteRepository.deleteById(id);
    }
    
    public Map<String, Object> exportarDatos(int id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", cliente.getNombre());
        datos.put("apellido", cliente.getApellido());
        datos.put("apellido2", cliente.getApellido2());
        datos.put("foto", cliente.getFoto());
        datos.put("correo", cliente.getCorreo());
        datos.put("telefono", cliente.getTelefono());
        datos.put("direccion", cliente.getDireccion());
        datos.put("numeroPerfiles", cliente.getNumeroPerfiles());
        return datos;
    }

    @Transactional
    public void eliminarCliente(int id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }
    
    public boolean existsById(int id) {
        return this.clienteRepository.existsById(id);
    }
}