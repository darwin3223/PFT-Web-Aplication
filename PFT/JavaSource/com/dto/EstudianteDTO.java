package com.dto;

import com.enums.EstadoEstudiante;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteDTO extends UsuarioDTO{
	
	private EstadoEstudiante estadoEstudiante;
	
	private int añoIngreso;

	
}
