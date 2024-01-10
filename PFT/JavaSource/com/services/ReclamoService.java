package com.services;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.authorization.rest.ReclamoDTOrest;
import com.dto.AccionReclamoDTO;
import com.dto.ReclamoDTO;
import com.entities.AccionReclamo;
import com.entities.EstadoSolicitud;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Itr;
import com.entities.Reclamo;
import com.entities.Semestre;
import com.enums.TipoReclamo;
import com.enums.TipoUsuario;
import com.exceptions.FieldValidationException;
import com.exceptions.MailException;
import com.exceptions.ServiciosException;
import com.repositories.ReclamoRepository;
import com.utils.MailUtil;
import com.validation.ValidationError;

import lombok.Data;
import java.io.Serializable;
import java.text.SimpleDateFormat;

@Data
@Stateless
@LocalBean
public class ReclamoService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private SemestreService semestreService;
	@Inject private AnalistaService analistaService;
	@Inject private EventoService eventoService;
	@Inject private EstudianteService estudianteService;
	@Inject private EstadoSolicitudService estadoService;
	
	@EJB private ReclamoRepository dao;
	
	public Reclamo save(ReclamoDTO dto) throws FieldValidationException, ServiciosException {
		ValidationError.validarReclamo(dto);
		Reclamo reclamoNuevo = dtoToReclamo(dto);
		reclamoNuevo.setEstado(estadoService.getAll().get(0));
		dao.save(reclamoNuevo);
		return reclamoNuevo;
	}
	
	public Reclamo update(ReclamoDTO dto) throws FieldValidationException, ServiciosException {
		ValidationError.validarReclamo(dto);
		Reclamo reclamo = dtoToReclamo(dto);
		dao.update(reclamo);
		return reclamo;
	}
	
	public void delete(long idReclamo) throws ServiciosException {
		
		Reclamo i = dao.get(idReclamo);
		if (i != null) {
			if (!i.getAcciones().isEmpty()) throw new ServiciosException("El reclamo que desea eliminar\nya cuenta con acciones tomadas por analistas.");
			
			dao.delete(i);
		}
	}
	
	public void addAccion(AccionReclamoDTO accionDto, ReclamoDTO reclamoDto, long idAnalista) throws FieldValidationException, ServiciosException, MailException {
		ValidationError.validarAccionReclamo(accionDto);
		Reclamo reclamo = buscarReclamoPorId(reclamoDto.getIdReclamo());
		AccionReclamo accion = convertDtoToAccionReclamo(accionDto);
		accion.setFechaHora(new Date());
		accion.setReclamo(reclamo);
		accion.setAnalista(analistaService.getAnalistaById(idAnalista));
		reclamo.getAcciones().add(accion);
		dao.saveAccion(accion, reclamo);
		
		SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
		String diaFormateado = formatoDia.format(new Date());
		SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
		String horaFormateada = formatoHora.format(new Date());
		String asunto = "Se realizó una acción en el reclamo que registraste";
		String mensaje = String.format("Estimado/a Usuario,\nLe informamos que el dia %s a las %s, uno de nuestros analistas realizó una acción en relación a su reclamo titulado '%s'.\nA continuación, se detalla la acción realizada:\n%s", 
				diaFormateado,
				horaFormateada, 
				reclamo.getTitulo(), 
				accion.getDetalle());
		MailUtil.mandarMail(reclamo.getEstudiante().getMailInstitucional(), mensaje, asunto);
	}
	
	public List<Reclamo> obtenerTodosReclamo(){
		return dao.getAll();
	}
	
	public Reclamo buscarReclamoPorId(long id) {
		return dao.get(id);
	}
	
	public AccionReclamo getLastAccionFromJustificacion(ReclamoDTO dto) {
		Reclamo reclamo = dtoToReclamo(dto);
		List<AccionReclamo> res = dao.getAccionByReclamo(reclamo);
		return !res.isEmpty() ? res.get(res.size()-1) : null;
	}
	
	public List<Reclamo> findByEstudiante(long idEstudiante) {
		Estudiante estudiante = estudianteService.getEstudianteById(idEstudiante);
		return dao.getByEstudiante(estudiante);
	}
	
	public Reclamo dtoToReclamo(ReclamoDTO DTO) {
		Reclamo reclamo = new Reclamo();
		reclamo.setIdReclamo(DTO.getIdReclamo());
		reclamo.setTitulo(DTO.getTitulo());
		reclamo.setTipoReclamo(DTO.getTipoReclamo());
		reclamo.setDetalle(DTO.getDetalle());
		reclamo.setFecha_Hora(DTO.getFecha_Hora());
		reclamo.setSemestre(DTO.getSemestre());
		reclamo.setEstudiante(DTO.getEstudiante());
		reclamo.setEvento(DTO.getEvento());
		reclamo.setEstado(DTO.getEstado());

		return reclamo;
	}
	
	public ReclamoDTO ReclamoTodto(Reclamo rec) {
		ReclamoDTO reclamoDTO = new ReclamoDTO();
		
		reclamoDTO.setIdReclamo(rec.getIdReclamo());
		reclamoDTO.setTitulo(rec.getTitulo());
		reclamoDTO.setTipoReclamo(rec.getTipoReclamo());
		reclamoDTO.setDetalle(rec.getDetalle());
		reclamoDTO.setFecha_Hora(rec.getFecha_Hora());
		reclamoDTO.setSemestre(rec.getSemestre());
		reclamoDTO.setEstado(rec.getEstado());
		reclamoDTO.setEstudiante(rec.getEstudiante());
		reclamoDTO.setEvento(rec.getEvento());
		
		return reclamoDTO;
	}
	
	public Reclamo dtoRestToReclamo(ReclamoDTOrest reclamoDtoRest) {
		Semestre semestre = semestreService.buscarPorId(reclamoDtoRest.getIdSemestre());
		Evento evento = eventoService.findById(reclamoDtoRest.getIdEvento());
		Estudiante estudiante = estudianteService.getEstudianteById(reclamoDtoRest.getIdEstudiante());
		
		Reclamo reclamo = new Reclamo();
		reclamo.setIdReclamo(reclamoDtoRest.getIdReclamo());
		reclamo.setTitulo(reclamoDtoRest.getTitulo());
		reclamo.setTipoReclamo(reclamoDtoRest.getTipoReclamo());
		reclamo.setDetalle(reclamoDtoRest.getDetalle());
		reclamo.setFecha_Hora(new Date());
		reclamo.setSemestre(semestre);
		EstadoSolicitud estado = estadoService.get(reclamoDtoRest.getIdEstado());
		reclamo.setEstado(estado);
		reclamo.setEstudiante(estudiante);
		reclamo.setEvento(evento);
		
		return reclamo;
	}
	
	public AccionReclamo convertDtoToAccionReclamo(AccionReclamoDTO dto) {
		return AccionReclamo.builder()
				.idAcc_Rec(dto.getIdAcc_Rec())
				.detalle(dto.getDetalle())
				.fechaHora(dto.getFechaHora())
				.analista(dto.getAnalista())
				.reclamo(dto.getReclamo())
				.build();
	}
	
	public AccionReclamoDTO convertAccionJustificacionToDto(AccionReclamo accion) {
		return AccionReclamoDTO.builder()
				.idAcc_Rec(accion.getIdAcc_Rec())
				.detalle(accion.getDetalle())
				.fechaHora(accion.getFechaHora())
				.analista(accion.getAnalista())
				.reclamo(accion.getReclamo())
				.build();
	}
	
	public List<Reclamo> getList(TipoUsuario tipoUsuario, long idUsuarioLogueado) {
		return TipoUsuario.ANALISTA.equals(tipoUsuario)
				? obtenerTodosReclamo()
				: findByEstudiante(idUsuarioLogueado);
	}
	
	public List<Reclamo> getByFiltros(Itr itr, String grupo, int generacion, int mes, TipoReclamo tipo){
		return dao.getByFiltros(itr, grupo, generacion, mes, tipo);
	}
	
	public Reclamo changeEstado(ReclamoDTO rec) throws ServiciosException {
		long idReclamo = rec.getIdReclamo();
		long idEstado = rec.getEstado().getIdEstado();
		
		Reclamo i = buscarReclamoPorId(idReclamo);
		if (i == null)	throw new ServiciosException("No se encontró el reclamo que desea modificar.");
		if (i.getEstado().getIdEstado() == idEstado) throw new ServiciosException("El reclamo ya cuenta con ese estado.");
		
		EstadoSolicitud s = estadoService.get(idEstado);
		if (s == null) throw new ServiciosException("No se encontró el estado.");
		
		i.setEstado(s);
		dao.update(i);
		return i;
	}

}
