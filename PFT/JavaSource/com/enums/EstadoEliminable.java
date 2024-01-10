package com.enums;

public enum EstadoEliminable {
	ELIMINABLE("Eliminable", 1), NO_ELIMINABLE("No Eliminable", 2);
	
	private String nombre;
	private int id;
	
	private EstadoEliminable(String nombre, int id) {
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
