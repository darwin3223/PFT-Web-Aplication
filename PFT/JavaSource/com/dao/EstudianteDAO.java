package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.entities.Estudiante;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class EstudianteDAO implements Repository<Estudiante> {

	@PersistenceContext	private EntityManager em;

    @Override
    public Estudiante get(long id) {
        return em.find(Estudiante.class, id);
    }

    @Override
    public List<Estudiante> getAll() {
        TypedQuery<Estudiante> query = em.createQuery("SELECT u FROM Estudiante u ORDER BY u.idUsuario ASC",
                Estudiante.class);
        return query.getResultList();
    }

    @Override
    public void save(Estudiante t) throws ServiciosException {
        try {
            em.persist(t);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServiciosException("No se pudo guardar el Estudiante");
        }
    }

    @Override
    public void update(Estudiante t) throws ServiciosException {
        try {
            em.merge(t);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServiciosException("No se pudo actualizar el Estudiante");
        }
    }

    @Override
    public void delete(Estudiante t) throws ServiciosException {
        try {
            em.remove(t);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServiciosException("No se pudo eliminar el Estudiante");
        }
    }
}