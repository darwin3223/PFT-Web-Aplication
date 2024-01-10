package com.authorization.rest;

import com.entities.EstadoSolicitud;
import com.enums.TipoReclamo;

import lombok.Data;

@Data
public class ReclamoDTOrest {
	private long idReclamo;
	
	private String titulo;

	private TipoReclamo tipoReclamo;

	private String detalle;

	private long idSemestre;

	private long idEstado;

	private long idEstudiante;

	private long idEvento;

}
