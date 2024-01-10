package com.dto;

import java.util.Date;
import java.util.List;

import com.entities.Itr;
import com.entities.Semestre;
import com.enums.EstadoEvento;
import com.enums.Modalidad;
import com.enums.TipoEvento;

import lombok.Data;

@Data
public class EventoDTO {
	private long idEvento;

	private String Titulo;

	private TipoEvento tipoEvento;
	
	private Date fecha_Hora_Inicio;

	private Date fecha_Hora_Final;
	
	private Modalidad modalidad;

	private String localizacion;
	
	private int creditos;
	
	private EstadoEvento estadoEvento;
	
	private String informacion;

	private Itr itr;

	private List<Semestre> listaSemestres;
}
