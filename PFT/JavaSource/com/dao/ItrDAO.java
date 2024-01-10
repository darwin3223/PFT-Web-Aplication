package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.entities.Itr;
import com.enums.EstadoItr;
import com.exceptions.ServiciosException;
import com.repositories.ItrRepository;
import com.repositories.Repository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class ItrDAO implements ItrRepository {
	
	@PersistenceContext	private EntityManager em;

	@Override
	public Itr get(long id) {
		return em.find(Itr.class, id);
	}

	@Override
	public List<Itr> getAll() {
		TypedQuery<Itr> query = em.createQuery("SELECT u FROM Itr u ORDER BY u.idItr ASC", Itr.class);
		return query.getResultList();
	}

	@Override
	public void save(Itr t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Itr");
		}
	}

	@Override
	public void update(Itr t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el Itr");
		}
	}

	@Override
	public void delete(Itr t) throws ServiciosException {
		try {
			t.setEstado(EstadoItr.DESACTIVADO);
			em.merge(t);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}
	
	public Itr findByName(String name)throws ServiciosException {
	   CriteriaBuilder cb = em.getCriteriaBuilder();
	   CriteriaQuery<Itr> query = cb.createQuery(Itr.class);
	   Root<Itr> root = query.from(Itr.class);
	
	   query.select(root).where(cb.equal(root.get("nombre"), name));
	
	   List<Itr> results = em.createQuery(query).getResultList();
	
	   if (results.isEmpty()) {
	        return null; // No se encontró ninguna entidad con ese nombre
	   } else {
	        return results.get(0); // Devuelve la primera entidad encontrada (puedes ajustar el comportamiento si hay múltiples resultados)
	   }
	    
	}
}
