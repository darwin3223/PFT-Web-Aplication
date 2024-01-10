package com.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.primefaces.shaded.json.JSONPropertyIgnore;

import com.enums.TipoReclamo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reclamo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private long idReclamo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private TipoReclamo tipoReclamo;
	
	@Column(nullable = true)
	@JoinColumn(name = "TITULO")
	private String titulo;
	
	@Column(nullable = true, length = 100)
	private String detalle;

	@Column(nullable = true, length = 30)
	private Date fecha_Hora;

	@ManyToOne
	@JoinColumn(name = "SEMESTRE")
	private Semestre semestre;
	
	@ManyToOne
	@JoinColumn(name= "IDESTADO")
	private EstadoSolicitud estado;

	@ManyToOne
	@JoinColumn(name = "IDESTUDIANTE")
	private Estudiante estudiante;

	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento evento;
	
	@ToString.Exclude
	@OneToMany(mappedBy="reclamo")
	@JsonIgnore
	private List<AccionReclamo> acciones;

}
