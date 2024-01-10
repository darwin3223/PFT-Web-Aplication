package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.entities.Departamento;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class DepartamentoService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="DepartamentoDAO")
	private Repository<Departamento> dao;

	public Departamento obtenerPorIdDepartamento(long idDepartamento) {
		return dao.get(idDepartamento);
	}
	
	public List<Departamento> obtenerTodosDepartamento() {
		return dao.getAll();
	}
	
	public void save(Departamento depto) throws ServiciosException {
		dao.save(depto);
	}
}
