package manyWorker.entity;

import java.util.List;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Trabajador extends Actor{

	private String nombreComercial;

	//Constructor
	public Trabajador(@NotBlank String nombre, @NotBlank String apellido, String apellido2, @URL String foto,
			@Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") String correo, @Pattern(regexp = "^[6-9][0-9]{8}$") String telefono,
			String direccion, List<PerfilSocial> numeroPerfiles, @NotBlank String nombreTrabajador) {
		super(nombre, apellido, apellido2, foto, correo, telefono, direccion, numeroPerfiles);
		
		//super.get() para recoger el nombre y apellido y formal el nombre comercial del trabajador
 		this.nombreComercial = super.getNombre() + " " + super.getApellido();
	}
	
	public Trabajador() {
        super();
    }
	
}
