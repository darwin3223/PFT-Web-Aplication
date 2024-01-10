package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;
import com.exceptions.ServiciosException;
import com.repositories.ConvocatoriaRepository;

import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class ConvocatoriaDAO implements ConvocatoriaRepository {

	@PersistenceContext protected EntityManager em;
	
	@Override
	public Convocatoria get(long id) {
		return em.find(Convocatoria.class, id);
	}

	@Override
	public List<Convocatoria> getAll() {
		TypedQuery<Convocatoria> query = em.createQuery("SELECT u FROM Convocatoria u ORDER BY u.idConvocatoria ASC", Convocatoria.class);
		return query.getResultList();
	}

	@Override
	public void save(Convocatoria t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Convocatoria t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Convocatoria t) throws ServiciosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Convocatoria> getByEstudiante(Estudiante estudiante) {
		TypedQuery<Convocatoria> query = em.createNamedQuery("convocatoria.findByEstudiante", Convocatoria.class);
		query.setParameter("estudiante", estudiante);
		return query.getResultList();
	}

	@Override
	public List<Convocatoria> getByEvento(Evento evento) {
		TypedQuery<Convocatoria> query = em.createNamedQuery("convocatoria.findByEvento", Convocatoria.class);
		query.setParameter("evento", evento);
		return query.getResultList();
	}

	@Override
	public List<Convocatoria> getByFilter(Estudiante estudiante) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Convocatoria> configConsulta = cb.createQuery(Convocatoria.class);
		Root<Convocatoria> raizConvocatoria = configConsulta.from(Convocatoria.class);
	//	raizConvocatoria.join("IDEVENTO", JoinType.LEFT);
		configConsulta.select(raizConvocatoria);
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (estudiante != null) {
			predicates.add(cb.equal(raizConvocatoria.get("estudiante"), estudiante));
		}
		
		configConsulta.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
	
		return em.createQuery(configConsulta).getResultList();
	}
	
}
