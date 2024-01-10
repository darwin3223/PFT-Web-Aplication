package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.entities.Evento;
import com.entities.Semestre;
import com.exceptions.ServiciosException;
import com.repositories.SemestreRepository;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class SemestreService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB private SemestreRepository dao;
	
	public List<Semestre> obtenerTodos(){
		return dao.getAll();
	}
	
	public List<Semestre> obtenerPorFiltros(Evento evento){
		return dao.getByFiltro(evento);
	}
	
	public Semestre buscarPorId(long id) {
		return dao.get(id);
	}
 	
	public void save(Semestre semestre) throws ServiciosException {
		dao.save(semestre);
	}
}
