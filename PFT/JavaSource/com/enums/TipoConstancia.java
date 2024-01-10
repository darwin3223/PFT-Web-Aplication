package com.enums;

public enum TipoConstancia {
	PRESENCIAL("Presencial",1),
	TRANSPORTE("Transporte",2),
	ESTUDIANTE_ACTIVO("Estudiante Activo",3),
	EXAMEN("Examen",4),
	CREDITO_VME("Credito VME",5),
	CREDITO_UTECInnova("Credito UTECInnova",6),
	CREDITO_OPTATIVAS("Credito Optativas",7);
	
	private String nombre;
	private int id;

	private TipoConstancia(String nombre, int id) {
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

