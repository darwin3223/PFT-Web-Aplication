package com.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;

import com.dto.AccionJustificacionDTO;
import com.dto.ConvocatoriaDTO;
import com.dto.JustificacionDTO;
import com.entities.AccionJustificacion;
import com.entities.Convocatoria;
import com.entities.EstadoSolicitud;
import com.entities.Evento;
import com.entities.Justificacion;
import com.enums.TipoUsuario;
import com.exceptions.FieldValidationException;
import com.exceptions.MailException;
import com.exceptions.ServiciosException;
import com.services.ConvocatoriaService;
import com.services.EstadoSolicitudService;
import com.services.JustificacionService;
import com.utils.MessagesUtil;
import com.utils.RenderVista;

import lombok.Data;

@Data
@ViewScoped
@Named(value = "gestionJustificacion")
public class JustificacionManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private AutenticacionManager autenticacion;
	@Inject private JustificacionService justificacionService;
	@Inject private ConvocatoriaService convocatoriaService;
	@Inject private EstadoSolicitudService estadoService;

	private List<Justificacion> listaJustificaciones;
	private List<EstadoSolicitud> listaEstados;
	
	private Long idEstadoFilter;
	private RenderVista render;
	private TipoUsuario tipoUsuario;	// Tipo del usuario Logeado
	private long idUsuario;				// Id del usuario logeado
	
	private JustificacionDTO justificacion;
	private JustificacionDTO newJustificacion;
	private AccionJustificacionDTO accion;
	

	@PostConstruct
	public void init() {
		idUsuario = autenticacion.loadIdUsuarioLogeado();
		tipoUsuario = autenticacion.loadTipoUsuarioLogeado(); 
		render = new RenderVista(tipoUsuario);
		idEstadoFilter = estadoService.getAll().get(0).getIdEstado();
		
		justificacion = JustificacionDTO.builder().evento(new Evento()).estado(new EstadoSolicitud()).build();
		newJustificacion = JustificacionDTO.builder().evento(new Evento()).build();
		accion = AccionJustificacionDTO.builder().build();
		
		listaEstados = estadoService.getAll();
		refreshListaJustificacion();
	}

	// Botones
	
	// Al presionar el botón Modificar Justificacion en JustificacionDataTable
	public void onSelectJustificacion(Justificacion justificacion) {
		this.justificacion = justificacionService.convertJustificacionToDto(justificacion);		
		AccionJustificacion result = justificacionService.getLastAccionFromJustificacion(this.justificacion);
		accion = result != null
				? justificacionService.convertAccionJustificacionToDto(result)
				: AccionJustificacionDTO.builder().build();
	}
	
	// Al presionar el botón Guardar en JustificacionDialogRegister
	public void onCreate() {
		try {
			justificacionService.save(newJustificacion, idUsuario);
			MessagesUtil.createInfoNotification("Justificación creada", "Se ha creado correctamente la justificacion");
			newJustificacion = JustificacionDTO.builder().evento(new Evento()).build();
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('JustificacionDialogRegistrar').hide()");
			onRefresh();
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al crear justificación", e.getMessage());
		}
	}
	
	// Al presionar el botón Guardar en JustificacionDialogModificar
	public void onUpdate() {
		try {
			Justificacion i = justificacionService.update(justificacion);
			MessagesUtil.createInfoNotification("Modificación satisfactoria", "Se ha actualizado correctamente la justificacion");
			for (Justificacion justificacion : listaJustificaciones) {
				if (justificacion.getIdJustificacion() == i.getIdJustificacion()) {
					justificacion.setDetalle(i.getDetalle());
					justificacion.setFechaHora(i.getFechaHora());
					break;
				}
			}
			refreshListaJustificacion();
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al modificar", e.getMessage());
		}
	}
	
	// Al presionar el botón Eliminar en JustificacionDataTable
	public void onDelete(long idJustificacion) {
		try {
			justificacionService.delete(idJustificacion);
			MessagesUtil.createInfoNotification("Baja satisfactoria", "Se ha eliminado correctamente la justificacion de inasistencia");
			for (Justificacion justificacion : listaJustificaciones) {
				if (justificacion.getIdJustificacion() == idJustificacion) {
					listaJustificaciones.remove(justificacion);
					Ajax.update("form");
					break;
				}
			}
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al eliminar", e.getMessage());
		}
	}

	// Al presionar el botón de Refrescar en JustificacionDataTable
	// Obtiene la lista de la Base de Datos y luego actualiza la vista
	public void onRefresh() {
		refreshListaJustificacion();
		Ajax.update("form");
	}
	
	// Al presionar el botón de Refrescar en JustificacionDataTable
	public void onAddAccion() {
		try {
			justificacionService.addAccion(accion, this.justificacion, idUsuario);
			MessagesUtil.createInfoNotification("Acción registrada", "Se ha registrado correctamente la acción sobre la justificacion");
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al crear acción", e.getMessage());
		} catch (MailException e) {
			MessagesUtil.createErrorNotification("Error!", "No se pudo notificar al estudiante sobre la acción realizada.");
		}
	}
	
	// Al presionar el botón de Actualizar en EstadoJustificacionDialog
	public void onUpdateEstado() {
		try {
			Justificacion i = justificacionService.changeEstado(justificacion);
			MessagesUtil.createInfoNotification("Modificación satisfactoria", "Se ha actualizado correctamente la justificacion");
			for (Justificacion justificacion : listaJustificaciones) {
				if (justificacion.getIdJustificacion() == i.getIdJustificacion()) {
					justificacion.setEstado(i.getEstado());
					break;
				}
			}
			Ajax.update("form");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al cambiar estado", e.getMessage());
		} 
		
	}
	
	// Otras funciones
	
	public void createJustificacion(ConvocatoriaDTO convocatoria) {
		newJustificacion = JustificacionDTO.builder()
				.estudiante(convocatoria.getEstudiante())
				.evento(convocatoria.getEvento())
				.build();
	}
	
	public List<Evento> refreshListaEventos() {
		List<Convocatoria> lista = convocatoriaService.findByEstudiante(idUsuario);
		List<Evento> listaEventos = new ArrayList<Evento>();
		for (Convocatoria convocatoria : lista) {
			listaEventos.add(convocatoria.getEvento());
		}
		return listaEventos;
	}
	
	public boolean globalFilterFunctionJustificacion(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		Justificacion justificacion = (Justificacion) value;
		return (justificacion.getIdJustificacion() + "").toLowerCase().contains(filterText)
				|| justificacion.getEvento().getTitulo().toLowerCase().contains(filterText)
				|| (justificacion.getEstudiante().getDocumento()+"").toLowerCase().contains(filterText);
	}

	public List<EstadoSolicitud> getStatuses(){
		return estadoService.getActives();
	}

	public void refreshListaJustificacion() {
		listaJustificaciones = justificacionService.getList(tipoUsuario, idUsuario);
		if (idEstadoFilter != null) {
			List<Justificacion> newList = new ArrayList<Justificacion>();
			for (Justificacion justificacion : listaJustificaciones){
				if (idEstadoFilter.longValue() == justificacion.getEstado().getIdEstado()) {
					newList.add(justificacion);
				}
			}
			listaJustificaciones.clear();
			listaJustificaciones.addAll(newList);
		}
		Ajax.update("form");
	}
}
