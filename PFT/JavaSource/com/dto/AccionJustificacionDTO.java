package com.dto;

import java.util.Date;

import com.entities.Analista;
import com.entities.Justificacion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccionJustificacionDTO {
	private long idAccion;

	private String detalle;

	private Date fechaHora;

	private Analista analista;

	private Justificacion justificacion;
}
