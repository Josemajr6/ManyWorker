package manyWorker.entity;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PerfilSocial extends DomainEntity{

	@NotBlank
	private String apodo;
	
	@NotBlank
	private String nombreRedSocial;
	
	@URL
	private String enlace;

	//Constructor
	public PerfilSocial(@NotBlank String apodo, @NotBlank String nombreRedSocial, @URL String enlace) {
		super();
		this.apodo = apodo;
		this.nombreRedSocial = nombreRedSocial;
		this.enlace = enlace;
	}

	//getter y setter
	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	public String getNombreRedSocial() {
		return nombreRedSocial;
	}

	public void setNombreRedSocial(String nombreRedSocial) {
		this.nombreRedSocial = nombreRedSocial;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}
	
	
	
}
