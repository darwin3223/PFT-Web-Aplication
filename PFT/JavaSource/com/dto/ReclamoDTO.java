package com.dto;

import java.util.Date;
import java.util.List;

import com.entities.AccionReclamo;
import com.entities.EstadoSolicitud;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Semestre;
import com.enums.TipoReclamo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReclamoDTO {
	private long idReclamo;
	
	private String titulo;

	private TipoReclamo tipoReclamo;

	private String detalle;

	private Date fecha_Hora;

	private Semestre semestre;

	private EstadoSolicitud estado;

	private Estudiante estudiante;

	private Evento evento;

	private List<AccionReclamo> acciones;
}
