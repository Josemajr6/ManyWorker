package manyWorker.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Categoria {
	
	//Comprobamos que el formato sea correcto por si se introduce manualmente
	@Id
	@Pattern(regexp = "\\d{6}-[A-Z0-9]{6}", message = "Formato de ID inválido (yyMMdd-XXXXXX)")
	private String id;
	
	@NotBlank
	private String titulo;
    
    private String leyesAplicables;

    private boolean esReparacion;

    @OneToMany
    private List<Tarea> tareas = new ArrayList<>();

    // Generador automático de ID antes de persistir si no está asignado
    @PrePersist
    public void generarId() {
        if (this.id == null || this.id.trim().isEmpty()) {
            String fecha = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMdd"));
            String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            this.id = fecha + "-" + random;
        }
    }

	public Categoria(String id, @NotBlank String titulo, String leyesAplicables, boolean esReparacion, List<Tarea> tareas) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.leyesAplicables = leyesAplicables;
		this.esReparacion = esReparacion;
		this.tareas = tareas;
	}

	public Categoria() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getLeyesAplicables() {
		return leyesAplicables;
	}

	public void setLeyesAplicables(String leyesAplicables) {
		this.leyesAplicables = leyesAplicables;
	}

	public boolean isEsReparacion() {
		return esReparacion;
	}

	public void setEsReparacion(boolean esReparacion) {
		this.esReparacion = esReparacion;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}    
}