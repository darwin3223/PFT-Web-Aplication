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
@Builder
@Table(name = "ACCION_RECLAMO")
@NamedQueries({
	@NamedQuery(name = "accionReclamo.findByReclamo", query = "SELECT a FROM AccionReclamo a WHERE a.reclamo = :reclamo")
})
public class AccionReclamo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long idAcc_Rec;

	@Column(nullable = false, length = 30)
	private Date fechaHora;

	@Column(nullable = false, length = 50)
	private String detalle;

	@ManyToOne(optional = false)
	@JoinColumn(name = "IDANALISTA")
	private Analista analista;

	@ManyToOne(optional = false)
	@JoinColumn(name = "IDRECLAMO")
	private Reclamo reclamo;

}
