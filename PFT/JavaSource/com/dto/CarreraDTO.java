package com.dto;


import com.entities.Itr;
import com.enums.EstadoCarrera;


import lombok.Data;

@Data
public class CarreraDTO {
	private long idCarreraEspecialidad;

	private String nombre;

	private Itr itr;
	
	private EstadoCarrera estadoCarreraEspecialidad;
}
