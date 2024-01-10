package com.repositories;

import java.util.List;

import com.entities.AccionReclamo;
import com.entities.Estudiante;
import com.entities.Itr;
import com.entities.Reclamo;
import com.enums.TipoReclamo;
import com.exceptions.ServiciosException;

public interface ReclamoRepository extends Repository<Reclamo> {
	
	List<Reclamo> getByEstudiante(Estudiante estudiante);

	List<AccionReclamo> getAccionByReclamo(Reclamo reclamo);
	
	AccionReclamo saveAccion(AccionReclamo accion, Reclamo reclamo) throws ServiciosException;
	
	List<Reclamo> getByFiltros(Itr itr, String grupo, int generacion, int mes, TipoReclamo tipo);
	
}
