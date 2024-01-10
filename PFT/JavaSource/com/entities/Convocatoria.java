package com.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.enums.TipoAsistencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="CONVOCATORIA_EVENTO")
@NamedQueries({
	@NamedQuery(name = "convocatoria.findAll", query = "SELECT i FROM Convocatoria i"),
	@NamedQuery(name = "convocatoria.findByEstudiante", query = "SELECT i FROM Convocatoria i WHERE i.estudiante = :estudiante"),
	@NamedQuery(name = "convocatoria.findByEvento", query = "SELECT i FROM Convocatoria i WHERE i.evento = :evento")
})
public class Convocatoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "IDCON_EVE")
	private long idConvocatoria;

	@Column(name = "CALIFICACION", nullable = false, length = 30)
	private float calificacion;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOASISTENCIA", nullable = false)
	private TipoAsistencia tipoAsistencia;

	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento evento;
	
	@ManyToOne
	@JoinColumn(name = "IDESTUDIANTE")
	private Estudiante estudiante;
}

