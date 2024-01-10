package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import com.entities.AccionJustificacion;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Justificacion;
import com.exceptions.ServiciosException;
import com.repositories.JustificacionRepository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class JustificacionDAO implements JustificacionRepository {
	
	@PersistenceContext protected EntityManager em;

	@Override
	public Justificacion get(long id) {
		return em.find(Justificacion.class, id);
	}

	@Override
	public List<Justificacion> getAll() {
		TypedQuery<Justificacion> query = em.createQuery("SELECT i FROM Justificacion i", Justificacion.class);
		return query.getResultList();
	}

	@Override
	public void save(Justificacion t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (EntityExistsException e) {
			throw new ServiciosException(e.getMessage());
		} catch (RollbackException e) {
			throw new ServiciosException(e.getMessage());
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}

	@Override
	public void update(Justificacion t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}

	@Override
	public void delete(Justificacion t) throws ServiciosException {
		try {
			em.remove(t);
			em.flush();
		} catch (EntityNotFoundException e) {
			throw new ServiciosException(e.getMessage());
		} catch (NoResultException e) {
			throw new ServiciosException(e.getMessage());
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}
	
	@Override
	public List<Justificacion> getByStudent(Estudiante estudiante) {
		TypedQuery<Justificacion> query = em.createNamedQuery("justificacion.getByEstudiante", Justificacion.class);
		query.setParameter("estudiante", estudiante);
		return query.getResultList();
	}
	
	@Override
	public List<Justificacion> getByEvento(Evento evento) {
		TypedQuery<Justificacion> query = em.createNamedQuery("justificacion.getByEvento", Justificacion.class);
		query.setParameter("evento", evento);
		return query.getResultList();
	}

	@Override
	public List<AccionJustificacion> findAccionByJustificacion(Justificacion justificacion) {
		TypedQuery<AccionJustificacion> query = em.createNamedQuery("accionJustificacion.findByJustificacion", AccionJustificacion.class);
		query.setParameter("justificacion", justificacion);
		return query.getResultList();
	}
	
	@Override
	public AccionJustificacion saveAction(AccionJustificacion accion, Justificacion justificacion) throws ServiciosException {
		try {
			em.persist(accion);
			em.merge(justificacion);
			em.flush();
			return accion;
		} catch (EntityExistsException e) {
			throw new ServiciosException(e.getMessage());
		} catch (RollbackException e) {
			throw new ServiciosException(e.getMessage());
		}
	}
}
