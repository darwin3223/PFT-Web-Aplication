package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.entities.Tutor;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class TutorDAO implements Repository<Tutor> {
	
	@PersistenceContext	private EntityManager em;
	
	@Override
    public Tutor get(long id) {
        return em.find(Tutor.class, id);
    }

    @Override
    public List<Tutor> getAll() {
        TypedQuery<Tutor> query = em.createQuery("SELECT u FROM Tutor u ORDER BY u.idUsuario ASC", Tutor.class);
        return query.getResultList();
    }

    @Override
    public void save(Tutor t) throws ServiciosException {
        try {
            em.persist(t);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServiciosException("No se pudo crear el tutor");
        }
    }

    @Override
    public void update(Tutor t) throws ServiciosException {
        try {
            em.merge(t);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServiciosException("No se pudo modificar el Tutor");
        }
    }

    @Override
    public void delete(Tutor t) throws ServiciosException {
        try {
            em.remove(t);
            em.flush();
        } catch (PersistenceException e) {
        	throw new ServiciosException("No se pudo eliminar el Tutor");
        }
    }

}
