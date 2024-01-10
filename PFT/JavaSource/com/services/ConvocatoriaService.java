package com.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dto.ConvocatoriaDTO;
import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;
import com.enums.TipoUsuario;
import com.repositories.ConvocatoriaRepository;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class ConvocatoriaService implements Serializable{
	private static final long serialVersionUID = 1L;

	@EJB private ConvocatoriaRepository dao;

	@Inject private EventoService eventoService;
	@Inject private EstudianteService estudianteService;
	
	// Esta función solo se utiliza para pruebas
	public List<Convocatoria> devFindAll() {
		return dao.getAll();
	}
	
	public List<Convocatoria> findByEstudiante(long idEstudiante){
		Estudiante estudiante = estudianteService.getEstudianteById(idEstudiante);
		return dao.getByEstudiante(estudiante);
	}
	
	public List<Convocatoria> findByEvento(long idEvento){
		Evento evento = eventoService.findById(idEvento);
		return dao.getByEvento(evento);
	}
	
	public List<Convocatoria> obtenerPorFiltros(Estudiante estudiante){
		return dao.getByFilter(estudiante);
	}
	
	public Convocatoria convertDtoToConvocatoria(ConvocatoriaDTO dto) {
		return Convocatoria.builder()
				.idConvocatoria(dto.getIdConvocatoria())
				.calificacion(dto.getCalificacion())
				.tipoAsistencia(dto.getTipoAsistencia())
				.evento(dto.getEvento())
				.estudiante(dto.getEstudiante())
				.build();
	}
	
	public ConvocatoriaDTO convertConvocatoriaToDto(Convocatoria convocatoria) {
		return ConvocatoriaDTO.builder()
				.idConvocatoria(convocatoria.getIdConvocatoria())
				.calificacion(convocatoria.getCalificacion())
				.tipoAsistencia(convocatoria.getTipoAsistencia())
				.evento(convocatoria.getEvento())
				.estudiante(convocatoria.getEstudiante())
				.build();
	}

	public List<Convocatoria> getList(TipoUsuario tipo, Estudiante estudiante){
		return TipoUsuario.ANALISTA.equals(tipo)
				? devFindAll()
				: obtenerPorFiltros(estudiante);
	}

}