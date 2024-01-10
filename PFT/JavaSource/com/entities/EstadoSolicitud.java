package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.enums.Activo;
import com.enums.EstadoEliminable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ESTADOSOLICITUD")
@Builder
public class EstadoSolicitud implements Serializable {
	private static final long serialVersionUID = 372899516509590991L;

	@Id
	@Column(name = "IDESTADOSOLICITUD")
	private long idEstado;
	
	@Column(name = "NOMBRE", nullable = false, unique = true)
	private String nombre;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ACTIVO", nullable = false)
	private Activo activo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ELIMINABLE", nullable = false)
	private EstadoEliminable eliminable;

}
