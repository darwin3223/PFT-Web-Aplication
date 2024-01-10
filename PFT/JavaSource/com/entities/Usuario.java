package com.entities;

import java.util.Date;

import javax.persistence.*;

import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="Usuario")
public class Usuario{
	
	@Id
	private long idUsuario;

	@Column(name = "NOMBRE", nullable = false)
	private String nombre;
	
	@Column(name = "APELLIDO", nullable = false)
	private String apellido;
	
	@Column(name = "DOCUMENTO", unique = true)
	private int documento;
	
	@Column(name = "FECHANACIMIENTO", nullable = false)
	private Date fechaNacimiento;
	
	@Column(name = "NOMBREUSUARIO", unique = true)
	private String nombreUsuario;
	
	@Column(name = "CONTRASENIA", nullable = false)
	private String contrasenia;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOUSUARIO", nullable = false)
	private TipoUsuario tipoUsuario;

	@Enumerated(EnumType.STRING)
	@Column(name = "VERIFICACION", nullable = false)
	private Verificacion verificacion;
	
	@Column(name = "MAIL", nullable = false)
	private String mail;
	
	@Column(name = "MAIL_INSTITUCIONAL", unique = true)//
	private String mailInstitucional;
	
	@Column(name = "TELEFONO", nullable = false)//
	private String telefono;
	
	@Column(name = "GENERO", nullable = false)//
	private String genero;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO", nullable = false)
	private EstadoUsuario estadoUsuario;//
	
	@ManyToOne
	@JoinColumn(name = "IDITR", nullable = false)//
	private Itr itr;
	
	@ManyToOne
	@JoinColumn(name = "IDLOCALIDAD", nullable = false)//
	private Localidad localidad;
}