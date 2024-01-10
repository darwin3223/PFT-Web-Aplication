package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.entities.Evento;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class EventoDAO implements Repository<Evento> {
	
	@PersistenceContext private EntityManager em;
	
	@Override
	public Evento get(long id) {
		return em.find(Evento.class, id);
	}

	@Override
	public List<Evento> getAll() {
		TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e", Evento.class);
		return query.getResultList();
	}

	@Override
	public void save(Evento t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Evento");
		}
	}

	@Override
	public void update(Evento t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el Evento");
		}
	}

	@Override
	public void delete(Evento t) throws ServiciosException {
		try {
			em.remove(t);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}
	
}
