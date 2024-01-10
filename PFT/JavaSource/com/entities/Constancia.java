package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import com.enums.TipoConstancia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Constancia implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private long idConstancia;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoConstancia tipoConstancia;
	
	@Column(nullable = false, length = 100)
	private String Detalle;

	@Column(nullable = false, length = 30)
	private Date fecha_Hora;
	
	@ManyToOne
	@JoinColumn(name= "IDESTADO")
	private EstadoSolicitud estado;
	
	@ManyToOne
	@JoinColumn(name = "IDESTUDIANTE")
	private Estudiante estudiante;

	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento evento;

	
}
