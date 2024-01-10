package com.repositories;

import java.util.List;

import com.entities.Evento;
import com.entities.Semestre;

public interface SemestreRepository extends Repository<Semestre> {

	List<Semestre> getByFiltro(Evento evento);
	
}
