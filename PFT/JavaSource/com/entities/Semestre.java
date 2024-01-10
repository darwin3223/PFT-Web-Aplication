package com.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Semestre implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private long idSemestre;
	
	private String nombre;
	
}
