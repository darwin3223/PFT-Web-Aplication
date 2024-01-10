package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.EstadoSolicitud;
import com.enums.Activo;
import com.exceptions.ServiciosException;
import com.repositories.EstadoRepository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class EstadoSolicitudDAO implements EstadoRepository {
	
	@PersistenceContext private EntityManager em;

	@Override
	public EstadoSolicitud get(long id) {
		return em.find(EstadoSolicitud.class, id);
	}

	@Override
	public List<EstadoSolicitud> getAll() {
		TypedQuery<EstadoSolicitud> query = em.createQuery("SELECT u FROM EstadoSolicitud u ORDER BY u.idEstado ASC", EstadoSolicitud.class);
		return query.getResultList();
	}

	@Override
	public void save(EstadoSolicitud t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}

	@Override
	public void update(EstadoSolicitud t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}

	@Override
	public void delete(EstadoSolicitud t) throws ServiciosException {
		try {
			t.setActivo(Activo.INACTIVO);
			em.merge(t);
			em.flush();
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}
	}

	@Override
	public List<EstadoSolicitud> getByEstado(Activo status) {
		TypedQuery<EstadoSolicitud> query = em.createQuery("SELECT u FROM EstadoSolicitud u WHERE u.activo = :estado", EstadoSolicitud.class);
		query.setParameter("estado", status);
		return query.getResultList();
	}
}
