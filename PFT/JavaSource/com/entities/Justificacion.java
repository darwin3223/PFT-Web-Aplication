package com.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "JUSTIFICACION")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedQueries({
	@NamedQuery(name = "justificacion.getAll", query = "SELECT i FROM Justificacion i"),
	@NamedQuery(name = "justificacion.getByEstudiante", query = "SELECT i FROM Justificacion i WHERE i.estudiante = :estudiante"),
	@NamedQuery(name = "justificacion.getByEvento", query = "SELECT i FROM Justificacion i WHERE i.evento = :evento")
})
public class Justificacion implements Serializable {
	private static final long serialVersionUID = 1L;	

	@Id
	private long idJustificacion;
	
	@Column(name = "DETALLE", nullable = false, length = 100)
	private String detalle;
	
	@Column(name = "FECHA_HORA", nullable = false, length = 30)
	private Date fechaHora;
	
	@ManyToOne
	@JoinColumn(name= "IDESTADO")
	private EstadoSolicitud estado;
	
	@ManyToOne
	@JoinColumn(name = "IDESTUDIANTE")
	private Estudiante estudiante;

	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento evento;
	
	@ToString.Exclude
	@OneToMany(mappedBy="justificacion")
	private List<AccionJustificacion> acciones;

}