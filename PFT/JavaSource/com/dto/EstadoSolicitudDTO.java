package com.dto;


import com.enums.EstadoEliminable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoSolicitudDTO {

	private long idEstado;
	private String nombre;
	private boolean activo;
	private EstadoEliminable eliminable;
}
