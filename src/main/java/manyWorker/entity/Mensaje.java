package manyWorker.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Mensaje extends DomainEntity {

	@NotBlank
	@ManyToOne
	private Actor remitente;
	
	@NotBlank
	@ManyToOne
	private Actor destinatario;
	
	@NotBlank
	private Date fechaEnvío;
	
	@NotBlank
	private String asunto;
	
	@NotBlank
	private String cuerpo;

	public Mensaje(Actor remitente, Actor destinatario, @NotBlank Date fechaEnvío, @NotBlank String asunto, @NotBlank String cuerpo) {
		super();
		this.remitente = remitente;
		this.destinatario = destinatario;
		this.fechaEnvío = fechaEnvío;
		this.asunto = asunto;
		this.cuerpo = cuerpo;
	}

	public Actor getRemitente() {
		return remitente;
	}

	public void setRemitente(Actor remitente) {
		this.remitente = remitente;
	}

	public Actor getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Actor destinatario) {
		this.destinatario = destinatario;
	}

	public Date getFechaEnvío() {
		return fechaEnvío;
	}

	public void setFechaEnvío(Date fechaEnvío) {
		this.fechaEnvío = fechaEnvío;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
}