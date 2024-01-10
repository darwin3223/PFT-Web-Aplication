package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACCION_CONSTANCIA")
public class AccionConstancia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long idAcc_Con;

	@Column(nullable = false, length = 30)
	private Date fechaHora;

	@Column(nullable = false, length = 50)
	private String detalle;

	@ManyToOne(optional = false)
	@JoinColumn(name = "IDANALISTA")
	private Analista analista;

	@ManyToOne(optional = false)
	@JoinColumn(name = "IDCONSTANCIA")
	private Constancia constancia;

}
