package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.enums.EstadoEstudiante;
import com.enums.EstadoUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Estudiante")
@PrimaryKeyJoinColumn(referencedColumnName="IDUSUARIO")
public class Estudiante extends Usuario implements Serializable{
	
	private static final long serialVersionUID = 5385063536424646834L;

	@Column(name = "ESTADOESTUDIANTE")
	@Enumerated(EnumType.STRING)
	private EstadoEstudiante estadoEstudiante;
	
	@Column(name = "ANNIOINGRESO")
	private int añoIngreso;
	
}
