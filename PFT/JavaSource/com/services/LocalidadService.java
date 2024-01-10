package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.entities.Departamento;
import com.entities.Localidad;
import com.exceptions.ServiciosException;
import com.repositories.LocalidadRepository;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class LocalidadService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB private LocalidadRepository dao;

	public Localidad buscarLocalidadPorId (long id) {
		return dao.get(id);
	}
	
	public List<Localidad> obtenerTodosLocalidad(){
		return dao.getAll();
	}
	public List<Localidad> obtenerLocalidadesPorDepartamento(Departamento departamento) {
		return dao.getByDepartamento(departamento);
	}

	public Localidad obtenerPorNombreLocalidad(String nombre) {
		return dao.getByNombre(nombre);
	}
	
	public void save(Localidad localidad) throws ServiciosException {
		dao.save(localidad);
	}
}