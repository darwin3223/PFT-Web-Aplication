package com.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;

import com.dto.EstadoSolicitudDTO;
import com.entities.EstadoSolicitud;
import com.enums.Activo;
import com.enums.EstadoEliminable;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.services.EstadoSolicitudService;
import com.utils.MessagesUtil;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionEstado")
public class EstadoSolicitudManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<EstadoSolicitud> listaEstados;
	
	private EstadoSolicitudDTO estado;
	private EstadoSolicitudDTO newEstado;
	
	private Activo activoFilter;
	
	@Inject private EstadoSolicitudService estadoService;
	
	@PostConstruct
	public void init() {
		listaEstados = estadoService.getAll();
		newEstado = EstadoSolicitudDTO.builder().build();
		estado = EstadoSolicitudDTO.builder().build();
	}
	
	/// Botones
	
	public void onSelectEstado(EstadoSolicitud estado) {
		this.estado = estadoService.convertEstadoToDto(estado);
	}
	
	public void onCreate() {
		try {
			estadoService.checkEstadoExist(listaEstados, newEstado.getNombre());
			estadoService.save(newEstado);
			newEstado = EstadoSolicitudDTO.builder().build();
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarEstado').hide()");
			MessagesUtil.createInfoMessage("Estado creado", "Se ha creado correctamente el estado");
			onRefresh();
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al crear el estado", e.getMessage());	
		}
	}
	
	public void onUpdate() {
		try {
			EstadoSolicitud e = estadoService.update(estado);
			for (EstadoSolicitud estado : listaEstados) {
				if (estado.getIdEstado() == e.getIdEstado()) {
					estado.setNombre(e.getNombre());
					estado.setActivo(e.getActivo());
					break;
				}
			}
			MessagesUtil.createInfoMessage("Estado modificado", "Se ha modificado correctamente el estado");
			Ajax.update("form");
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al modificar el estado", e.getMessage());
		}
	}
	
	public void onDelete(long id) throws FieldValidationException {
		try {
			estadoService.delete(id);
			for (EstadoSolicitud estado : listaEstados) {
				if (estado.getIdEstado() == id) {
					estado.setActivo(Activo.INACTIVO);
					break;
				}
			}
			MessagesUtil.createInfoNotification("Estado eliminado lógicamente", "Se ha eliminado lógicamente el estado");
			Ajax.update("form");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al eliminar estado", e.getMessage());
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error al eliminar estado", e.getMessage());
		}
	}
	
	public void onRefresh() {
		listaEstados = estadoService.getAll();
		Ajax.update("form");
	}
	
	public void refreshListaEstado(Activo activo) {
		listaEstados = new LinkedList<>();
		if (activo !=null) {
			for (EstadoSolicitud estado : estadoService.getAll()) {
				if (estado.getActivo().equals(activo)) {
					listaEstados.add(estado);
				}
			}
		}else {
			listaEstados = estadoService.getAll();
		}
		Ajax.update("estadoDatatable");
	}
	
	/// Otros
	
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		boolean filterTextCondition = !LangUtils.isBlank(filterText);

		EstadoSolicitud estado = (EstadoSolicitud) value;
		boolean activoCondition = activoFilter == null || estado.getActivo().equals(activoFilter);
		
		if (filterTextCondition) {
			boolean idCondition = (estado.getIdEstado() + "").toLowerCase().contains(filterText);
			boolean nombreCondition = estado.getNombre().toLowerCase().contains(filterText);
			return activoCondition && (idCondition || nombreCondition);
		} else {
			return activoCondition;
		}
	}
	
	public Activo[] getStatuses() {
        return Activo.values();
    }

	// Obtiene un color dependiendo del 'estado' para usar en el componente Tag Severity de la vista
	public String getTagSeverity(Activo activo) {
		return 	Activo.ACTIVO.equals(activo) ? "success"
			:	Activo.INACTIVO.equals(activo) ? "danger"
			:	"info";
	}
}
