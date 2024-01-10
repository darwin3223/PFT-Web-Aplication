package com.repositories;

import java.util.List;

import com.entities.EstadoSolicitud;
import com.enums.Activo;

public interface EstadoRepository extends Repository<EstadoSolicitud> {
	
    List<EstadoSolicitud> getByEstado(Activo status);

}
