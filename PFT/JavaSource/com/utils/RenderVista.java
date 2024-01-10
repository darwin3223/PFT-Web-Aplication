package com.utils;

import com.enums.TipoUsuario;

import lombok.Data;

@Data
public class RenderVista {
	private boolean estudiante;
	private boolean tutor;
	private boolean analista;
	
	public RenderVista(TipoUsuario tipo) {
		this.estudiante = TipoUsuario.ESTUDIANTE.equals(tipo);
		this.analista = TipoUsuario.ANALISTA.equals(tipo);
		this.tutor = TipoUsuario.TUTOR.equals(tipo);
	}
}
