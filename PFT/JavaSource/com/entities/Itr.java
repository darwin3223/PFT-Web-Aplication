package com.entities;

import javax.persistence.*;

import com.enums.EstadoItr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Itr {
	@Id
	private long idItr;
	
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "ID_LOCALIDAD")
	private Localidad localidad;
	
	@Column(name = "ESTADOITR")
	@Enumerated(EnumType.STRING)
	private EstadoItr estado;
}
