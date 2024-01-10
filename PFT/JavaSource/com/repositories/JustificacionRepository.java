package com.repositories;

import java.util.List;

import com.entities.AccionJustificacion;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Justificacion;
import com.exceptions.ServiciosException;

public interface JustificacionRepository extends Repository<Justificacion> {
	
    List<Justificacion> getByStudent(Estudiante estudiante);

    List<Justificacion> getByEvento(Evento evento);

    List<AccionJustificacion> findAccionByJustificacion(Justificacion justificacion);

    AccionJustificacion saveAction(AccionJustificacion accion, Justificacion justificacion) throws ServiciosException;

}
