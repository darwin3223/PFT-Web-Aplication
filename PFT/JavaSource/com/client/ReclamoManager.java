package com.client;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;

import com.dto.AccionReclamoDTO;
import com.dto.ConvocatoriaDTO;
import com.dto.ReclamoDTO;
import com.entities.AccionReclamo;
import com.entities.Convocatoria;
import com.entities.EstadoSolicitud;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Reclamo;
import com.entities.Semestre;
import com.enums.TipoUsuario;
import com.exceptions.FieldValidationException;
import com.exceptions.MailException;
import com.exceptions.ServiciosException;
import com.services.ConvocatoriaService;
import com.services.EstadoSolicitudService;
import com.services.EstudianteService;
import com.services.EventoService;
import com.services.ReclamoService;
import com.services.SemestreService;
import com.utils.MessagesUtil;
import com.utils.RenderVista;
import com.validation.ValidationError;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Data
@ViewScoped
@Named(value = "gestionReclamo")
public class ReclamoManager implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject private EventoService eventoService;
	@Inject	private UsuarioManager gestionUsuario;
	@Inject	private ReclamoService reclamoService;
	@Inject private SemestreService semestreService;
	@Inject private EstadoSolicitudService estadosSolicitudService;
	@Inject	private EstudianteService estudianteService;
	@Inject	private ConvocatoriaService convocatoriaService;
	@Inject private AutenticacionManager gestionAutenticacion;
	
	private ReclamoDTO reclamoSeleccionado;
	
	private List<Reclamo> listaReclamos;
	private List<Evento> listaEventos;
	private List<Semestre> listaSemestres;
	private List<EstadoSolicitud> listaEstados;
	
	private TipoUsuario tipoUsuario;
	
	private AccionReclamoDTO accion;
	
	private Long idEstadoFilter;
	
	private RenderVista render;
	
	private EstadoSolicitud estadoFilter;
	
	private long idEventoSeleccionado;
	private long idSemestreSeleccionado;
	private long idEstadoCJRSeleccionado;
	
	private boolean renderButtonModificarReclamo;
	private boolean renderListaSemestres;
	
	private long idUsuarioLogueado;
	
	private ConvocatoriaDTO convocatoriaSeleccionada;
	
	private String valueListaSemestres = "Seleccione un evento para ver los semestres";
	
	@PostConstruct
	public void init() {
		idUsuarioLogueado = gestionAutenticacion.loadIdUsuarioLogeado();
		reclamoSeleccionado = ReclamoDTO.builder().evento(new Evento()).estado(new EstadoSolicitud()).build();
		listaEstados = estadosSolicitudService.getAll();
		tipoUsuario = gestionAutenticacion.loadTipoUsuarioLogeado(); 
		render = new RenderVista(tipoUsuario);
		listaEventos = new LinkedList<>();
		listaSemestres = semestreService.obtenerTodos();
		renderButtonModificarReclamo = false;
		renderListaSemestres = false;
		accion = AccionReclamoDTO.builder().build();
		cargarEventos();
		idEstadoFilter = estadosSolicitudService.getAll().get(0).getIdEstado();
		refreshListaReclamos();
	}
	
	public boolean globalFilterFunctionReclamo(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		Reclamo reclamo = (Reclamo) value;
		return (reclamo.getIdReclamo() + "").toLowerCase().contains(filterText)
				|| reclamo.getTitulo().toLowerCase().contains(filterText)
				|| reclamo.getEstudiante().getNombre().toLowerCase().contains(filterText)
				|| (reclamo.getEstudiante().getDocumento()+"").toLowerCase().contains(filterText)
				|| reclamo.getEvento().getTitulo().toLowerCase().contains(filterText)
				|| reclamo.getEstado().getNombre().toLowerCase().contains(filterText)
				|| reclamo.getEvento().getTipoEvento().getNombre().toLowerCase().contains(filterText);
	}
	
	public void seleccionarReclamo(Reclamo reclamo) {
		reclamoSeleccionado = reclamoService.ReclamoTodto(reclamo);
		idEventoSeleccionado = reclamo.getEvento().getIdEvento();
		idSemestreSeleccionado = reclamo.getSemestre().getIdSemestre();
		idEstadoCJRSeleccionado = reclamo.getEstado().getIdEstado();
		Evento eve = eventoService.findById(reclamo.getEvento().getIdEvento());
		listaSemestres = eve.getListaSemestres();
		idEstadoCJRSeleccionado = reclamo.getEstado().getIdEstado();
		AccionReclamo result = reclamoService.getLastAccionFromJustificacion(reclamoSeleccionado);
		accion = result != null
				? reclamoService.convertAccionJustificacionToDto(result)
				: AccionReclamoDTO.builder().build();
	}

	public void cargarEventos() {
		Estudiante est = estudianteService.getEstudianteById(idUsuarioLogueado);
		List<Convocatoria> listaConvocatorias = convocatoriaService.obtenerPorFiltros(est);
		for (Convocatoria convocatoria : listaConvocatorias) {
			listaEventos.add(convocatoria.getEvento());
		}
	}
	
	public void cargarListaSemestres(){
		Evento evento = eventoService.findById(idEventoSeleccionado);
		
		if (evento != null) {
			valueListaSemestres = "Seleccione un semestre";
			listaSemestres = evento.getListaSemestres();
		}else{
			valueListaSemestres = "Seleccione un evento para ver los semestres";
			listaSemestres = new LinkedList<>();
		}
		
	}
	
	public void registrarReclamo() {
		reclamoSeleccionado.setEvento(convocatoriaSeleccionada.getEvento());
		reclamoSeleccionado.setEstudiante(convocatoriaSeleccionada.getEstudiante());
		Semestre semestre = semestreService.buscarPorId(idSemestreSeleccionado);
		reclamoSeleccionado.setSemestre(semestre);
		
		EstadoSolicitud estado = estadosSolicitudService.getAll().get(0);
		reclamoSeleccionado.setEstado(estado);
		reclamoSeleccionado.setFecha_Hora(new Date());
		
		try {
			reclamoService.save(reclamoSeleccionado);
			MessagesUtil.createInfoNotification("Registro de reclamo correcto", "El reclamo se registro correctamente.");
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('ReclamoDialogRegistrar').hide()");
			refreshListaReclamos();
			Ajax.update("form");
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al registrar reclamo", e.getMessage());
		}
	}
	
	public void modificarReclamo() {
		Evento eve = eventoService.findById(idEventoSeleccionado);
		reclamoSeleccionado.setEvento(eve);
		
		Semestre sem = semestreService.buscarPorId(idSemestreSeleccionado);
		reclamoSeleccionado.setSemestre(sem);
		
		EstadoSolicitud est = estadosSolicitudService.get(idEstadoCJRSeleccionado);
		reclamoSeleccionado.setEstado(est);
		reclamoSeleccionado.setFecha_Hora(new Date());
		try {
			ValidationError.validarReclamo(reclamoSeleccionado);
			reclamoService.update(reclamoSeleccionado);
			MessagesUtil.createInfoNotification("Reclamo modificado", "El reclamo fue modificado correctamente.");
			refreshListaReclamos();
			Ajax.update("form");
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al actualizar reclamo", e.getMessage());
		}
	}
	
	public void eliminarReclamo(long id) {
		try {
			reclamoService.delete(id);
			listaReclamos = reclamoService.obtenerTodosReclamo();
			MessagesUtil.createInfoNotification("Baja exitosa", "Se ha eliminado de forma satisfactoria el Reclamo");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error", e.getMessage());
		}
		refreshListaReclamos();
		
		Ajax.update("form");
		
	}
	
	public void verBotonesReclamos() {
		if (TipoUsuario.ESTUDIANTE.equals(gestionAutenticacion.loadTipoUsuarioLogeado())) {
			renderButtonModificarReclamo = true;
		}else {
			renderButtonModificarReclamo = false;
		}
	}
	
	public void limpiarCampos() {
		reclamoSeleccionado = new ReclamoDTO();
		idEstadoCJRSeleccionado = 0;
		idEventoSeleccionado = 0;
		idSemestreSeleccionado = 0;
	}
	
	public void onClickReclamo(ConvocatoriaDTO convocatoria) {
		idEventoSeleccionado = convocatoria.getEvento().getIdEvento();
		convocatoriaSeleccionada = convocatoria;
		cargarListaSemestres();
		
	}
	
	public List<EstadoSolicitud> getEstados() {
        return estadosSolicitudService.getAll();
    }
	
	public void onAddAccion() {
		try {
			reclamoService.addAccion(accion, this.reclamoSeleccionado, idUsuarioLogueado);
			MessagesUtil.createInfoNotification("Acción registrada", "Se ha registrado correctamente la acción sobre el reclamo");
			Ajax.update("form");
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al registrar acción", e.getMessage());
		} catch (MailException e) {
			MessagesUtil.createWarnNotification("Problemas al notificar usuario", "No se pudo notificar al estudiante sobre la acción realizada.");
		}
	}
	
	public void onUpdateEstado() {
		try {
			EstadoSolicitud estado = estadosSolicitudService.get(idEstadoCJRSeleccionado);
			reclamoSeleccionado.setEstado(estado);
			Reclamo i = reclamoService.changeEstado(reclamoSeleccionado);
			MessagesUtil.createInfoNotification("Modificación satisfactoria", "Se ha actualizado correctamente el Reclamo");
			for (Reclamo rec : listaReclamos) {
				if (rec.getIdReclamo() == i.getIdReclamo()) {
					rec.setEstado(i.getEstado());
					break;
				}
			}
			Ajax.update("form");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al cambiar estado", e.getMessage());
		} 
		
	}
	
	public void refreshListaReclamos() {
		listaReclamos = reclamoService.getList(tipoUsuario, idUsuarioLogueado);
		if (idEstadoFilter != null) {
			List<Reclamo> newList = new ArrayList<Reclamo>();
			for (Reclamo rec : listaReclamos){
				if (idEstadoFilter.longValue() == rec.getEstado().getIdEstado()) {
					newList.add(rec);
				}
			}
			
			listaReclamos.clear();
			listaReclamos.addAll(newList);

		}
		Ajax.update("form");
	}
	
}
