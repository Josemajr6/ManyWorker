package manyWorker.entity;

import java.util.List;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Actor extends DomainEntity{
	
	@NotBlank
	private String nombre;
	
	@NotBlank
	private String apellido;
	
	private String apellido2;
	
	@URL 
	private String foto;
	
	@Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
	private String correo;
	
	@Pattern(regexp = "^[6-9][0-9]{8}$")
	private String telefono;
	
	private String direccion;
	
	@OneToMany
	private List<PerfilSocial> numeroPerfiles;

	
	//Constructor
	public Actor(@NotBlank String nombre, @NotBlank String apellido, String apellido2, @URL String foto,
			@Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") String correo, @Pattern(regexp = "^[6-9][0-9]{8}$") String telefono,
			String direccion, List<PerfilSocial> numeroPerfiles) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.apellido2 = apellido2;
		this.foto = foto;
		this.correo = correo;
		this.telefono = telefono;
		this.direccion = direccion;
		this.numeroPerfiles = numeroPerfiles;
	}
	
	//Constructor
	public Actor() {
		super();
	}
	
	//Getter y setter
	    
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public List<PerfilSocial> getNumeroPerfiles() {
		return numeroPerfiles;
	}

	public void setNumeroPerfiles(List<PerfilSocial> numeroPerfiles) {
		this.numeroPerfiles = numeroPerfiles;
	}
	
}
