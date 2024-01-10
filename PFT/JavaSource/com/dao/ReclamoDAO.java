package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.entities.AccionReclamo;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Itr;
import com.entities.Reclamo;
import com.enums.TipoReclamo;
import com.exceptions.ServiciosException;
import com.repositories.ReclamoRepository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class ReclamoDAO implements ReclamoRepository {
	
	@PersistenceContext private EntityManager em;
	
	@Override
	public Reclamo get(long id) {
		return em.find(Reclamo.class, id);
	}

	@Override
	public List<Reclamo> getAll() {
		TypedQuery<Reclamo> query = em.createQuery("SELECT u FROM Reclamo u ORDER BY u.idReclamo ASC", Reclamo.class);
		return query.getResultList();
	}

	@Override
	public void save(Reclamo t) throws ServiciosException {
		try {
			em.persist(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el reclamo");
		}
	}

	@Override
	public void update(Reclamo t) throws ServiciosException {
		try {
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el Reclamo");
		}
	}

	@Override
	public void delete(Reclamo t) throws ServiciosException {
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
	public List<Reclamo> getByEstudiante(Estudiante estudiante) {
		TypedQuery<Reclamo> query = em.createQuery("SELECT i FROM Reclamo i WHERE i.estudiante = :estudiante", Reclamo.class)
				.setParameter("estudiante", estudiante);
		return query.getResultList();
	}

	@Override
	public List<AccionReclamo> getAccionByReclamo(Reclamo reclamo) {
		TypedQuery<AccionReclamo> query = em.createNamedQuery("accionReclamo.findByReclamo", AccionReclamo.class)
				.setParameter("reclamo", reclamo);
		return query.getResultList();
	}

	@Override
	public AccionReclamo saveAccion(AccionReclamo accion, Reclamo reclamo) throws ServiciosException {
		try {
			em.persist(accion);
			em.merge(reclamo);
			em.flush();
			return accion;
		} catch (EntityExistsException e) {
			throw new ServiciosException(e.getMessage());
		} catch (RollbackException e) {
			throw new ServiciosException(e.getMessage());
		}
	}

	@Override
	public List<Reclamo> getByFiltros(Itr itr, String grupo, int generacion, int mes, TipoReclamo tipo) {
	    CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<Reclamo> query = cb.createQuery(Reclamo.class);
	    
	    Root<Reclamo> reclamoRoot = query.from(Reclamo.class);
	    
	    Join<Reclamo, Evento> eventoJoin = reclamoRoot.join("evento", JoinType.INNER);
	    
	    Join<Reclamo, Estudiante> estudianteJoin = reclamoRoot.join("estudiante", JoinType.INNER);
	    
	    List<Predicate> predicates = new ArrayList<>();
	    
	    if (itr != null) {
	        predicates.add(cb.equal(eventoJoin.get("itr"), itr));
	    }
	    
//	    if (grupo != null) {
//	        predicates.add(cb.equal(reclamoRoot.get("grupo"), grupo));
//	    }
	    
	    if (generacion != -1) {
	        predicates.add(cb.equal(estudianteJoin.get("añoIngreso"), generacion));
	    }
	    
	    if (mes > 0 && mes <= 12) {
	        Expression<Integer> monthExpression = cb.function("MONTH", Integer.class, reclamoRoot.get("fecha_Hora"));
	        predicates.add(cb.equal(monthExpression, mes));
	    }
	    
	    if (tipo != null) {
	        predicates.add(cb.equal(reclamoRoot.get("tipoReclamo"), tipo));
	    }
	    
	    Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
	    query.where(finalPredicate);
	    
	    TypedQuery<Reclamo> typedQuery = em.createQuery(query);
	    List<Reclamo> reclamos = typedQuery.getResultList();
	    return reclamos;
	}
}
