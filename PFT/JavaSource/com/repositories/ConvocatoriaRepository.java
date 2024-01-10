package com.repositories;

import java.util.List;

import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;

public interface ConvocatoriaRepository extends Repository<Convocatoria> {

	List<Convocatoria> getByEstudiante(Estudiante estudiante);
	
	List<Convocatoria> getByEvento(Evento evento);
	
	List<Convocatoria> getByFilter(Estudiante estudiante);
	
}
