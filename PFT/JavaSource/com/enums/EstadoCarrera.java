package com.enums;

public enum EstadoCarrera {
ACTIVADO("Activado", 1), DESACTIVADO("Desactivado", 2);
	
	private String nombre;
	private int id;
	
	private EstadoCarrera(String nombre, int id) {
		this.nombre = nombre;
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	public int getId() {
		return id;
	}
}
