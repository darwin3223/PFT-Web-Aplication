package com.services;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dto.AccionJustificacionDTO;
import com.dto.JustificacionDTO;
import com.entities.AccionJustificacion;
import com.entities.EstadoSolicitud;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Justificacion;
import com.enums.TipoUsuario;
import com.exceptions.FieldValidationException;
import com.exceptions.MailException;
import com.exceptions.ServiciosException;
import com.validation.ValidationError;
import com.repositories.JustificacionRepository;
import com.utils.MailUtil;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class JustificacionService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB private JustificacionRepository dao;
	
	@Inject private AnalistaService analistaService;
	@Inject private EstudianteService estudianteService;
	@Inject private EventoService eventoService;
	@Inject private EstadoSolicitudService estadoService;
	
	public Justificacion save(JustificacionDTO dto, long idUsuario) throws FieldValidationException, ServiciosException {
		ValidationError.validarJustificacion(dto);
		Justificacion justificacion = convertDtoToJustificacion(dto);
		justificacion.setFechaHora(new Date());
		justificacion.setEstudiante(dto.getEstudiante());
		justificacion.setEstado(estadoService.getAll().get(0));
		dao.save(justificacion);
		return justificacion;
	}
	
	public Justificacion update(JustificacionDTO dto) throws FieldValidationException, ServiciosException {
		ValidationError.validarJustificacion(dto);
		Justificacion i = dao.get(dto.getIdJustificacion());
		i.setDetalle(dto.getDetalle());
		i.setFechaHora(dto.getFechaHora());
		dao.update(i);
		return i;
	}
	
	public void delete(long idJustificacion) throws ServiciosException {
		Justificacion i = dao.get(idJustificacion);
		if (i != null) {
			if (!i.getAcciones().isEmpty())	throw new ServiciosException("La justificación de inasistencia que desea eliminar\nya cuenta con acciones tomadas por analistas.");
			
			dao.delete(i);
		}
	}
	
	public void addAccion(AccionJustificacionDTO accionDto, JustificacionDTO justificacionDto, long idAnalista) throws FieldValidationException, ServiciosException, MailException {
		ValidationError.validarAccionJustificacion(accionDto);
		Justificacion justificacion = findById(justificacionDto.getIdJustificacion());
		AccionJustificacion accion = convertDtoToAccionJustificacion(accionDto);
		accion.setFechaHora(new Date());
		accion.setJustificacion(justificacion);
		accion.setAnalista(analistaService.getAnalistaById(idAnalista));
		justificacion.getAcciones().add(accion);
		dao.saveAction(accion, justificacion);
		
		SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
		String diaFormateado = formatoDia.format(new Date());
		
		SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
		String horaFormateada = formatoHora.format(new Date());
		String asunto = "Se realizó una acción en la justificación que registraste";
		
		
		String mensaje = String.format("Estimado/a Usuario,\nLe informamos que el dia %s a las %s, uno de nuestros analistas realizó una acción en relación a su justificación de insasistencia del Evento '%s'.\nA continuación, se detalla la acción realizada:\n%s", 
				diaFormateado,
				horaFormateada, 
				justificacion.getEvento().getTitulo(), 
				accion.getDetalle());

		MailUtil.mandarMail(justificacion.getEstudiante().getMailInstitucional(), mensaje, asunto);
	}
	
	public Justificacion changeEstado(JustificacionDTO justificacion) throws ServiciosException {
		long idJustificacion = justificacion.getIdJustificacion();
		long idEstado = justificacion.getEstado().getIdEstado();
		
		Justificacion i = findById(idJustificacion);
		if (i == null)	throw new ServiciosException("No se encontró la justificación que desea modificar.");
		if (i.getEstado().getIdEstado() == idEstado) throw new ServiciosException("La justificación ya cuenta con ese estado.");
		
		EstadoSolicitud s = estadoService.get(idEstado);
		if (s == null) throw new ServiciosException("No se encontró el estado.");
		
		i.setEstado(s);
		dao.update(i);
		return i;
	}
	
	public Justificacion findById(long idJustificacion) {
		return dao.get(idJustificacion);
	}
	
	public List<Justificacion> findAll() {
		return dao.getAll();
	}
	
	public List<Justificacion> findByEstudiante(long idEstudiante) {
		Estudiante estudiante = estudianteService.getEstudianteById(idEstudiante);
		return dao.getByStudent(estudiante);
	}
	
	public List<Justificacion> findByEvento(long idEvento) {
		Evento evento = eventoService.findById(idEvento);
		return dao.getByEvento(evento);
	}
	
	public AccionJustificacion getLastAccionFromJustificacion(JustificacionDTO dto) {
		Justificacion justificacion = convertDtoToJustificacion(dto);
		List<AccionJustificacion> res = dao.findAccionByJustificacion(justificacion);
		return !res.isEmpty() ? res.get(res.size()-1) : null;
	}
	
	public Justificacion convertDtoToJustificacion(JustificacionDTO dto) {
		return Justificacion.builder()
				.idJustificacion(dto.getIdJustificacion())
				.detalle(dto.getDetalle())
				.fechaHora(dto.getFechaHora())
				.estado(dto.getEstado())
				.estudiante(dto.getEstudiante())
				.evento(dto.getEvento())
				.build();
	}
	
	public JustificacionDTO convertJustificacionToDto(Justificacion justificacion) {
		return JustificacionDTO.builder()
				.idJustificacion(justificacion.getIdJustificacion())
				.detalle(justificacion.getDetalle())
				.fechaHora(justificacion.getFechaHora())
				.estado(justificacion.getEstado())
				.estudiante(justificacion.getEstudiante())
				.evento(justificacion.getEvento())
				.build();
	}
	
	public AccionJustificacionDTO convertAccionJustificacionToDto(AccionJustificacion accion) {
		return AccionJustificacionDTO.builder()
				.idAccion(accion.getIdAccion())
				.detalle(accion.getDetalle())
				.fechaHora(accion.getFechaHora())
				.analista(accion.getAnalista())
				.justificacion(accion.getJustificacion())
				.build();
	}
	
	public AccionJustificacion convertDtoToAccionJustificacion(AccionJustificacionDTO dto) {
		return AccionJustificacion.builder()
				.idAccion(dto.getIdAccion())
				.detalle(dto.getDetalle())
				.fechaHora(dto.getFechaHora())
				.analista(dto.getAnalista())
				.justificacion(dto.getJustificacion())
				.build();
	}

	public List<Justificacion> getList(TipoUsuario tipo, long idUsuario){
		return TipoUsuario.ANALISTA.equals(tipo)
				? findAll()
				: findByEstudiante(idUsuario);
	}
}
