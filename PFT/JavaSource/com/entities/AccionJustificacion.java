package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACCION_JUSTIFICACION")
@Builder
@NamedQueries({
	@NamedQuery(name = "accionJustificacion.findByJustificacion", query = "SELECT a FROM AccionJustificacion a WHERE a.justificacion = :justificacion ORDER BY a.idAccion")
})
public class AccionJustificacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "IDACC_JUS")
	private long idAccion;

	@Column(name = "DETALLE", nullable = false, length = 50)
	private String detalle;

	@Column(name = "FECHA_HORA", nullable = false, length = 30)
	private Date fechaHora;

	@ManyToOne(optional = false)
	@JoinColumn(name = "IDANALISTA")
	private Analista analista;

	@ManyToOne(optional = false)
	@JoinColumn(name = "IDJUSTIFICACION", nullable = false)
	private Justificacion justificacion;

}
