package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.entities.Departamento;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class DepartamentoDAO implements Repository<Departamento> {
	
	@PersistenceContext	private EntityManager em;

	@Override
	public Departamento get(long id) {
		return em.find(Departamento.class, id);
	}

	@Override
	public List<Departamento> getAll() {
		TypedQuery<Departamento> query = em.createQuery("SELECT u FROM Departamento u ORDER BY u.idDepartamento ASC", Departamento.class);
		return query.getResultList();
	}

	@Override
	public void save(Departamento t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Departamento");
		}
	}

	@Override
	public void update(Departamento t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Departamento t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}
}
