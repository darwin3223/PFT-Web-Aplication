package com.client;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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

import com.dto.EstudianteDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Departamento;
import com.entities.Estudiante;
import com.entities.Itr;
import com.entities.Localidad;
import com.entities.Usuario;
import com.enums.EstadoEstudiante;
import com.enums.EstadoItr;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.services.AnalistaService;
import com.services.DepartamentoService;
import com.services.EstudianteService;
import com.services.ItrService;
import com.services.LocalidadService;
import com.services.TutorService;
import com.services.UsuarioService;
import com.utils.MessagesUtil;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionListarUsuarios")
public class ListarUsuariosManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject	private ItrService itrService;
	@Inject	private TutorService tutorService;
	@Inject private UsuarioService usuarioService;
	@Inject	private AnalistaService analistaService;
	@Inject	private LocalidadService localidadService;
	@Inject	private EstudianteService estudianteService;
	@Inject private DepartamentoService departamentoService;
	
	private UsuarioManager gestionUsuario = new UsuarioManager();

	private boolean vistaTutor = false;
	private boolean vistaEstudiante = false;
	
	private List<Usuario> listaUsuarios = new LinkedList<>();
	private List<Localidad> listaLocalidades;
	
	private Usuario usuarioSeleccionado = new Usuario();
	private UsuarioDTO usuarioNuevo = new UsuarioDTO();
	private EstudianteDTO estudianteNuevo = new EstudianteDTO();
	private TutorDTO tutorNuevo = new TutorDTO();
	
	private EstadoUsuario estadoFilter;
	private int generacionFilter;
	// VARIABLES PARA DIALOG MODIFICAR USUARIO
	private String estadoUsuarioSeleccionado;
	private String verificacionSeleccionado;
	private long idSeleccionada;
	private long idItrSeleccionado;
	private long idDepartamentoSeleccionado;
	private long idLocalidadSeleccionada;
	private boolean inputsVisMod;
	
	private String textLocalidad = "Seleccione un departamento antes";

	// VARIABLES PARA DIALOG REGISTRAR USUARIO
	private String tipoUsuarioSeleccionado = null;
	private boolean renderInputText = false;
	
	private boolean renderInputEstudiante;
	private boolean renderInputTutor;
	
	@PostConstruct
	public void init() {
		listaUsuarios = usuarioService.getUsuarios();
		usuarioNuevo.setItr(new Itr());
		usuarioNuevo.setDepartamento(new Departamento());
		usuarioNuevo.setLocalidad(new Localidad());
	}
	
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		boolean filterTextCondition = !LangUtils.isBlank(filterText);

		Usuario usu = (Usuario) value;
		boolean estadoCondition = estadoFilter == null || usu.getEstadoUsuario().equals(estadoFilter);
		
		if (filterTextCondition) {
			boolean idCondition = (usu.getIdUsuario() + "").toLowerCase().contains(filterText);
			boolean nombreCondition = usu.getNombre().toLowerCase().contains(filterText);
			boolean apellidoCondition = usu.getApellido().toLowerCase().contains(filterText);
			boolean documentoCondition = (usu.getDocumento() + "").toLowerCase().contains(filterText);
			boolean itrCondition = usu.getItr().getNombre().toLowerCase().contains(filterText);
			boolean tipoCondition = usu.getTipoUsuario().getNombre().toLowerCase().contains(filterText);
			boolean verificacionCondition = usu.getVerificacion().getNombre().toLowerCase().contains(filterText);
			return estadoCondition && (idCondition || itrCondition || nombreCondition || apellidoCondition || documentoCondition || tipoCondition || verificacionCondition);
		} else {
			return estadoCondition;
		}
	}
	
	public void borrarUsuario(long id) {
		try {
			usuarioService.deleteUsuarioLogical(id);
			cargarListarUsuarios();
		} catch (ServiciosException e) {
			MessagesUtil.createErrorMessage("Error al eliminar", e.getMessage());
		}
	}

	public List<Itr> cargarListaItr() {
		List<Itr> itrs = new ArrayList<Itr>();
		for (Itr itr : itrService.getAll()) {
			if (EstadoItr.ACTIVADO.equals(itr.getEstado())) {
				itrs.add(itr);
			}
		}
		return itrs;
	}
	
	public String anioParaCalendario() {
		LocalDate fechaActual = LocalDate.now();
		int a = fechaActual.getYear() - 100;
		int b = fechaActual.getYear() + 10;
		return a + ":" + b;
	}
	
	public String cargarListarUsuarios() {
		listaUsuarios = usuarioService.getUsuarios();
		return null;
	}

	/*
	 * FUNCIONES PARA DIALOG MODIFICAR USUARIO
	 * Las siguientes funciones son para dar funcionalidad al Modificar usuario dentro
	 * de el Listado de Usuarios al que puede acceder el Analista
	 */
	
	// Se ejecuta al presionar en el botón de modificar usuario en el listado.
	public void seleccionarUsuario() {
		limpiarCampos();
		
		idItrSeleccionado = usuarioSeleccionado.getItr().getIdItr();
		idDepartamentoSeleccionado = usuarioSeleccionado.getLocalidad().getDepartamento().getIdDepartamento();
		idLocalidadSeleccionada = usuarioSeleccionado.getLocalidad().getIdLocalidad();
		
		estudianteNuevo = new EstudianteDTO();
		tutorNuevo = new TutorDTO();
		usuarioNuevo.setIdUsuario(usuarioSeleccionado.getIdUsuario());
		usuarioNuevo.setNombre(usuarioSeleccionado.getNombre());
		usuarioNuevo.setApellido(usuarioSeleccionado.getApellido());
		usuarioNuevo.setDocumento(usuarioSeleccionado.getDocumento() + "");
		usuarioNuevo.setFechaNacimiento(usuarioSeleccionado.getFechaNacimiento());
		usuarioNuevo.setMail(usuarioSeleccionado.getMail());
		usuarioNuevo.setMailInstitucional(usuarioSeleccionado.getMailInstitucional());
		usuarioNuevo.setTelefono(usuarioSeleccionado.getTelefono());
		usuarioNuevo.setTipoUsuario(usuarioSeleccionado.getTipoUsuario());
		usuarioNuevo.setVerificacion(usuarioSeleccionado.getVerificacion());
		usuarioNuevo.setItr(usuarioSeleccionado.getItr());
		usuarioNuevo.setNombreUsuario(usuarioSeleccionado.getNombreUsuario());
		usuarioNuevo.setContrasenia(usuarioSeleccionado.getContrasenia());
		usuarioNuevo.setEstadoUsuario(usuarioSeleccionado.getEstadoUsuario());
		usuarioNuevo.setLocalidad(usuarioSeleccionado.getLocalidad());
		usuarioNuevo.setGenero(usuarioSeleccionado.getGenero());
		usuarioNuevo.setDepartamento(usuarioNuevo.getLocalidad().getDepartamento());
		
		vistaEstudiante = usuarioSeleccionado.getTipoUsuario() == TipoUsuario.ESTUDIANTE;
		vistaTutor = usuarioSeleccionado.getTipoUsuario() == TipoUsuario.TUTOR;
		
		if(vistaEstudiante) {
			Estudiante estudiante = estudianteService.getEstudianteById(usuarioSeleccionado.getIdUsuario());
			estudianteNuevo.setAñoIngreso(estudiante.getAñoIngreso());
			estudianteNuevo.setEstadoEstudiante(estudiante.getEstadoEstudiante());
		}
		if (vistaTutor) {
			tutorNuevo.setArea(tutorService.getTutorById(usuarioSeleccionado.getIdUsuario()).getArea());
			tutorNuevo.setRolTutor(tutorService.getTutorById(usuarioSeleccionado.getIdUsuario()).getRolTutor());
		}
		cargarListaLocalidadesInput();
	}
	
	// Se ejecuta al presionar el botón de guardar en el dialogo de modificar usuario.
	public void modificarUsuario() {
		usuarioNuevo.setItr(itrService.get(idItrSeleccionado));
		usuarioNuevo.setDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		usuarioNuevo.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		
		try {
			if(vistaEstudiante) {
				estudianteService.updateEstudiante(usuarioNuevo, estudianteNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
				MessagesUtil.createInfoNotification("Usuario estudiante modificado", "El usuario fue modificado correctamente.");
				limpiarCampos();
			}else if(vistaTutor) {
				tutorService.updateTutor(usuarioNuevo, tutorNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
				MessagesUtil.createInfoNotification("Modificación de tutor correcto", "El usuario tutor fue modificado correctamente.");
				limpiarCampos();
			}else {
				analistaService.updateAnalista(usuarioNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
				MessagesUtil.createInfoMessage("Usuario analista modificado", "El usuario fue modificado correctamente.");
				limpiarCampos();
			}
			cargarListarUsuarios();
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al modificar usuario", "Asegúrate de que los datos ingresados del estudiante son correctos.");
		}
	}
	
	/*
	 * FUNCIONES PARA DIALOG REGISTRAR USUARIO
	 * Las siguientes funciones son para dar funcionalidad al Registar usuario dentro
	 * de el Listado de Usuarios al que puede acceder el Analista
	 */
	
	public void limpiarCampos() {
		usuarioNuevo = new UsuarioDTO();
		estudianteNuevo = new EstudianteDTO();
		tutorNuevo = new TutorDTO();
		idDepartamentoSeleccionado = 0;
		idItrSeleccionado = 0;
		idLocalidadSeleccionada = 0;
	}
	
	public void menuItemChanged(String tipoUsu) {
		if ("ESTUDIANTE".equals(tipoUsu)) {
			renderInputEstudiante = true;
			renderInputTutor = false;


		} else if ("TUTOR".equals(tipoUsu)) {
			renderInputEstudiante = false;
			renderInputTutor = true;

		} else {
			renderInputEstudiante = false;
			renderInputTutor = false;
		}
		
	}
	
	public void registrarUsuario() {
		usuarioNuevo.setItr(itrService.get(idItrSeleccionado));
		usuarioNuevo.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		usuarioNuevo.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuarioNuevo.setVerificacion(Verificacion.NO_VERIFICADO);
		
		estudianteNuevo.setEstadoEstudiante(EstadoEstudiante.NO_MATRICULADO);
		
		try {
			if (usuarioNuevo.getTipoUsuario().equals(TipoUsuario.TUTOR)) {
				tutorService.saveTutor(usuarioNuevo, tutorNuevo);
				MessagesUtil.createInfoNotification("Registro de tutor correcto", "El tutor se registró correctamente");
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");
				limpiarCampos();
			} else if (usuarioNuevo.getTipoUsuario().equals(TipoUsuario.ESTUDIANTE)) {
				estudianteService.saveEstudiante(usuarioNuevo, estudianteNuevo);
				MessagesUtil.createInfoNotification("Registro de estudiante correcto", "El estudiante se registró correctamente");
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");
				limpiarCampos();
			} else {
				analistaService.saveAnalista(usuarioNuevo);
				MessagesUtil.createInfoNotification("Registro de analista correcto", "El analista se registró correctamente");
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");
				limpiarCampos();
			}
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al registrar usuario", "El usuario no se pudo registrar, Asegúrate de que los datos ingresados son correctos o inténtalo más tarde.");
		}
	}
		
	public List<Departamento> cargarListaDepartamento(){
		return departamentoService.obtenerTodosDepartamento();
	}
	
	public void cargarListaLocalidadesInput() {
		listaLocalidades = localidadService.obtenerLocalidadesPorDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		
		if (listaLocalidades.size() > 0) {	
			textLocalidad = "Seleccione una localidad";
		} else {
			listaLocalidades = new ArrayList<>();
			textLocalidad = "Seleccione un departamento antes";
		}
		
	}
	
	public void refreshListaUsuarios() {
		listaUsuarios = usuarioService.getUsuarios();
		Ajax.update("seccion_lista_usuarios");
	}
	
	public String getTagSeverity(EstadoUsuario estado) {
		return 	EstadoUsuario.ACTIVO.equals(estado) ? "success" : "danger"; 
	}
	
	public String getTagSeverity(Verificacion verificacion) {
		return 	Verificacion.VERIFICADO.equals(verificacion) ? "success" : "danger"; 
	}
	
	public void refreshListaUsuarios(EstadoUsuario estado) {
		listaUsuarios = new LinkedList<>();
		if (estado != null) {
			for (Usuario usu : usuarioService.getUsuarios()) {
				if (usu.getEstadoUsuario().equals(estado)) {
					listaUsuarios.add(usu);
				}
			}
		}else {
			listaUsuarios = usuarioService.getUsuarios();
		}
		Ajax.update("usuarios");
	}
	
	public void filtrarPorGen() {
		listaUsuarios = new LinkedList<>();
		if (generacionFilter != -1) {
			for (Usuario usu : usuarioService.getUsuarios()) {
				if (usu.getTipoUsuario().equals(TipoUsuario.ESTUDIANTE)) {
					Estudiante est = estudianteService.getEstudianteById(usu.getIdUsuario());
					if (est.getAñoIngreso() == generacionFilter) {
						listaUsuarios.add(usu);
					}
				}
			}
		}else {
			listaUsuarios = usuarioService.getUsuarios();
		}
		Ajax.update("usuarios");
	}
}