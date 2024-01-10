package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dto.EstadoSolicitudDTO;
import com.entities.EstadoSolicitud;
import com.enums.Activo;
import com.enums.EstadoEliminable;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.repositories.EstadoRepository;
import com.validation.ValidationError;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class EstadoSolicitudService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB private EstadoRepository dao;
	
	public EstadoSolicitud get(long id) {
		return dao.get(id);
	}
	
	public List<EstadoSolicitud> getAll() {
		return dao.getAll();
	}
	
	public List<EstadoSolicitud> getActives() {
		return dao.getByEstado(Activo.ACTIVO);
	}
	
	public EstadoSolicitud save(EstadoSolicitudDTO dto) throws FieldValidationException, ServiciosException {
		ValidationError.validarEstado(dto);
		EstadoSolicitud estado = convertDtoToEstado(dto);
		estado.setEliminable(EstadoEliminable.ELIMINABLE);
		dao.save(estado);
		return estado;
	}
	
	public EstadoSolicitud update(EstadoSolicitudDTO dto) throws FieldValidationException, ServiciosException {
		ValidationError.validarEstado(dto);
		EstadoSolicitud estado = get(dto.getIdEstado());
		estado.setNombre(dto.getNombre());
		estado.setActivo(dto.isActivo() ? Activo.ACTIVO : Activo.INACTIVO);
		dao.update(estado);
		return estado;
	}

	public void delete(long id) throws ServiciosException, FieldValidationException {
		EstadoSolicitud estado = get(id);
		if (estado.getEliminable().equals(EstadoEliminable.ELIMINABLE)) {
			dao.delete(estado);
		}else {
			throw new FieldValidationException("Este estado es uno de los principales y no se puede eliminar.");
		}
		
	}
	
	public EstadoSolicitud convertDtoToEstado(EstadoSolicitudDTO dto) {
		return EstadoSolicitud.builder()
				.idEstado(dto.getIdEstado())
				.nombre(dto.getNombre())
				.activo(dto.isActivo() ? Activo.ACTIVO : Activo.INACTIVO)
				.build();
	}
	
	public EstadoSolicitudDTO convertEstadoToDto(EstadoSolicitud estado) {
		return EstadoSolicitudDTO.builder()
				.idEstado(estado.getIdEstado())
				.nombre(estado.getNombre())
				.activo(Activo.ACTIVO.equals(estado.getActivo()))
				.build();
	}
	
	public void checkEstadoExist(List<EstadoSolicitud> lista, String nombre) throws FieldValidationException {
		for (EstadoSolicitud estado : lista) {
			if (estado.getNombre().equals(nombre)) {
				throw new FieldValidationException("Ya existe un estado con ese nombre, por favor seleccione otro nombre para el nuevo estado.");
			}
		}
	}
}
