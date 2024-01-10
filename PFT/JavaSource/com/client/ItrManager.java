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

import com.dto.ItrDTO;
import com.entities.Departamento;
import com.entities.Itr;
import com.entities.Localidad;
import com.enums.EstadoItr;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.services.DepartamentoService;
import com.services.ItrService;
import com.services.LocalidadService;
import com.utils.MessagesUtil;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionItr")
public class ItrManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject	private ItrService itrService;
	@Inject private DepartamentoService departamentoService;
	@Inject private LocalidadService localidadService;

	private ItrDTO itrSeleccionado;
	private long idLocalidadSeleccionada;
	private ItrDTO newItr;
	
	private List<Itr> listaItrs;
	private List<Departamento> listaDepartamentos;
	private List<Localidad> listaLocalidades;
	
	private EstadoItr estadoFilter;
	
	@PostConstruct
	private void init() {
		listaItrs = itrService.getAll();
		listaDepartamentos = departamentoService.obtenerTodosDepartamento();
		itrSeleccionado = ItrDTO.builder()
				.departamento(new Departamento())
				.localidad(new Localidad())
				.build();
		newItr = ItrDTO.builder()
				.departamento(new Departamento())
				.localidad(new Localidad())
				.build();
	}
	
	/// Botones
	
	// Al presionar el botón Modificar Itr en ItrDataTable 
	public void onSelectItr(Itr itr) {
		itrSeleccionado = itrService.itrToDTO(itr);
		cargarListaLocalidades(itrSeleccionado.getDepartamento().getIdDepartamento());
		idLocalidadSeleccionada = itr.getLocalidad().getIdLocalidad();
		Ajax.update("form:dialogModificarItrContent");
	}
	
	// Al presionar el botón Guardar en ItrDialogRegistrar
	public void onCreate() {
		newItr.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		
		try {
			Itr itr = itrService.save(newItr);
			listaItrs.add(itr);
			newItr = ItrDTO.builder().departamento(new Departamento()).localidad(new Localidad()).build();
			Ajax.update("form");
			Ajax.update("dialogs:dialogRegistrarItrContent");
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarItr').hide()");
			MessagesUtil.createInfoNotification("Registro exitoso", "Se ha registrado de forma satisfactoria el ITR");
			idLocalidadSeleccionada = -1;
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
			Ajax.update("form");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al registrar el ITR", e.getMessage());
			Ajax.update("form");
		}
	}
	
	// Al presionar el botón Guardar en ItrDialogModificar
	public void onUpdate() {
		itrSeleccionado.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		
		try {
			Itr updatedItr = itrService.update(itrSeleccionado);
			for (Itr itr : listaItrs) {
				if (itr.getIdItr() == updatedItr.getIdItr()) {
					itr.setNombre(updatedItr.getNombre());
					itr.setIdItr(updatedItr.getIdItr());
					itr.setLocalidad(updatedItr.getLocalidad());
					itr.setEstado(updatedItr.getEstado());
					break;
				}
			}
			MessagesUtil.createInfoNotification("Modificación exitosa", "Se ha modificado de forma satisfactoria el ITR");	
			Ajax.update("form");
			idLocalidadSeleccionada = -1;
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
			Ajax.update("form");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al modificar el ITR", e.getMessage());
			Ajax.update("form");
		}
	}
	
	// Al presionar el botón Eliminar en ItrDastaTable
	public void onDelete(long id) {
		try {
			itrService.deleteLogical(id);
			for (Itr itr : listaItrs) {
				if (itr.getIdItr() == id) {
					itr.setEstado(EstadoItr.DESACTIVADO);
					break;
				}
			}
			MessagesUtil.createInfoNotification("Baja exitosa", "Se ha eliminado de forma satisfactoria el ITR");
			Ajax.update("form");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al eliminar el ITR", e.getMessage());
			Ajax.update("form");
		}
	}
	
	// Al presionar el botón Actualizar en ItrDataTable
	public void onRefresh() {
		listaItrs = itrService.getAll();
		Ajax.update("form");
	}
	
	
	/// Otros
	
	public void cargarListaLocalidades(long idDepartamento) {
		Departamento departamento = departamentoService.obtenerPorIdDepartamento(idDepartamento);
		listaLocalidades = localidadService.obtenerLocalidadesPorDepartamento(departamento);
	}
	
	public void refreshListaItr(EstadoItr estado) {
		listaItrs = new LinkedList<>();
		if (estado != null) {
			for (Itr itr : itrService.getAll()) {
				if (itr.getEstado().equals(estado)) {
					listaItrs.add(itr);
				}
			}
		}else {
			listaItrs = itrService.getAll();
		}
		Ajax.update("form");
	}
	
	public void limpiarCampos() {
		itrSeleccionado = ItrDTO.builder().build();
		newItr = ItrDTO.builder().build();
	}
	
	// Obtiene un color dependiendo del 'estado' para usar en el componente Tag Severity de la vista
	public String getTagSeverity(EstadoItr estado) {
		return 	EstadoItr.ACTIVADO.equals(estado) ? "success" : "danger"; 
	}

	public boolean globalFilterFunctionItr(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		boolean filterTextCondition = !LangUtils.isBlank(filterText);

		Itr itr = (Itr) value;
		boolean estadoCondition = estadoFilter == null || itr.getEstado().equals(estadoFilter);
		
		if (filterTextCondition) {
			boolean idCondition = (itr.getIdItr() + "").toLowerCase().contains(filterText);
			boolean itrCondition = itr.getNombre().toLowerCase().contains(filterText);
			boolean localidadCondition = itr.getLocalidad().getNombre().toLowerCase().contains(filterText);
			boolean departamentoCondition = itr.getLocalidad().getDepartamento().getNombre().toLowerCase().contains(filterText);
			return estadoCondition && (idCondition || itrCondition || localidadCondition || departamentoCondition);
		} else {
			return estadoCondition;
		}
	}
}
