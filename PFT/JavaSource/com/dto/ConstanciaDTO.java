package com.dto;

import java.util.Date;

import com.entities.EstadoSolicitud;
import com.entities.Estudiante;
import com.entities.Evento;
import com.enums.TipoConstancia;

import lombok.Data;

@Data
public class ConstanciaDTO {
	private long idConstancia;

	private TipoConstancia tipoConstancia;

	private String Detalle;

	private Date fecha_Hora;

	private EstadoSolicitud estado;

	private Estudiante estudiante;

	private Evento evento;
}
