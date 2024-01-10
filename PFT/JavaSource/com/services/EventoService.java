package com.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.entities.Evento;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class EventoService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="EventoDAO")
	private Repository<Evento> dao;
	
	public Evento findById(long idEvento) {
		return dao.get(idEvento);
	}
	
	public List<Evento> getAll(){
		return dao.getAll();
	}
	
	public void save(Evento eve) throws ServiciosException {
		dao.save(eve);
	}

}
