package manyWorker.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Tutorial extends DomainEntity {

	// Atributos
	
	@NotBlank
	private String titulo;
	
	@UpdateTimestamp // Esto es para que la fecha se actualice sola cada vez que guardas
	private LocalDateTime fechaActualizacion;
	
	@NotBlank
	private String resumen;
	
	@ElementCollection // Para crear una tabla separada para esta lista
	private List<String> imagenes;
	
	@NotBlank
	@Column(columnDefinition = "TEXT") // Si la cadena de texto es muy larga, neceistamos estos atributo
	private String texto;

	// Constructor vacío
	public Tutorial() {
		super();
	}

	// Getters y Setters
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public List<String> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<String> imagenes) {
		this.imagenes = imagenes;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
}
