package com.dto;
import com.entities.Estudiante;
import com.entities.Evento;
import com.enums.TipoAsistencia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvocatoriaDTO {
	private long idConvocatoria;

	private float calificacion;

	private TipoAsistencia tipoAsistencia;

	private Evento evento;

	private Estudiante estudiante;
}
