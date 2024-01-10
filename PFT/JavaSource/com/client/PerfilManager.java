package com.client;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.dto.EstudianteDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Estudiante;
import com.entities.Tutor;
import com.entities.Usuario;
import com.enums.TipoUsuario;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.services.AnalistaService;
import com.services.EstudianteService;
import com.services.ItrService;
import com.services.TutorService;
import com.services.UsuarioService;
import com.utils.MessagesUtil;
import com.utils.RenderVista;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionPerfil")
public class PerfilManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private TipoUsuario tipoUsuario;	// Tipo del usuario Logeado
	private long idUsuario;				// Id del usuario logeado
	private RenderVista render;
	
	private long idItrSeleccionado;
	
	@Inject	private AnalistaService analistaService;
	@Inject	private TutorService tutorService;
	@Inject	private EstudianteService estudianteService;
	@Inject	private UsuarioService usuarioService;
	@Inject	private ItrService itrService;
	@Inject	private AutenticacionManager autenticacion;
	
	private UsuarioDTO usuario = new UsuarioDTO();
	private EstudianteDTO estudiante = new EstudianteDTO();
	private TutorDTO tutor = new TutorDTO();
	private Usuario usuarioLogeado;
	private Estudiante estudianteLogueado;
	private Tutor tutorLogueado;
	
	private String passwordNueva;
	
	@PostConstruct
	public void init() {
		idUsuario = autenticacion.loadIdUsuarioLogeado();
		tipoUsuario = autenticacion.loadTipoUsuarioLogeado();
		render = new RenderVista(tipoUsuario);
		cargarDatosUsuario();
	}
	
	public void modificarContraseña() {
		try {
			usuarioService.updatePassword(usuarioLogeado.getIdUsuario(), passwordNueva);
			MessagesUtil.createInfoNotification("Contraseña modificada", "La contraseña fue modificada correctamente.");
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al cambiar la contraseña", e.getMessage());
		}
	}
	
	public void modificarDatosPropios() {
		try {
			// TODO
			if (usuario.getTipoUsuario().equals(TipoUsuario.ESTUDIANTE)) {
				Estudiante estudiante = estudianteService.getEstudianteById(usuario.getIdUsuario());
				estudianteService.updateEstudiante(usuario, estudianteService.estudianteToDTO(estudiante));
				MessagesUtil.createInfoNotification("Usuario estudiante modificado", "El usuario fue modificado correctamente.");
			} else if (usuario.getTipoUsuario().equals(TipoUsuario.TUTOR)) {
				Tutor tutor = tutorService.getTutorById(usuario.getIdUsuario());
				tutorService.updateTutor(usuario, tutorService.tutorToDTO(tutor));
				MessagesUtil.createInfoNotification("Modificación de tutor correcto", "El usuario tutor fue modificado correctamente.");
			} else if (usuario.getTipoUsuario().equals(TipoUsuario.ANALISTA)) {
				analistaService.updateAnalista(usuario);
				MessagesUtil.createInfoMessage("Usuario analista modificado", "El usuario fue modificado correctamente.");
			}
			cargarDatosUsuario();
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al modificar usuario", "Asegúrate de que los datos ingresados del estudiante son correctos.");
		}
	}
	
	public void cargarDatosUsuario() {
		usuarioLogeado = usuarioService.getUsuarioById(idUsuario);
		estudianteLogueado = estudianteService.getEstudianteById(idUsuario);
		tutorLogueado = tutorService.getTutorById(idUsuario);
		
		if (tutorLogueado != null) {
			tutor.setArea(tutorLogueado.getArea());
			tutor.setRolTutor(tutorLogueado.getRolTutor());
		}
		if (estudianteLogueado != null) {
			estudiante.setAñoIngreso(estudianteLogueado.getAñoIngreso());
			estudiante.setEstadoEstudiante(estudianteLogueado.getEstadoEstudiante());
		}
		
		usuario = usuarioService.convertUsuariotoDto(usuarioLogeado);
		usuario.setIdUsuario(usuarioLogeado.getIdUsuario());
		
	}
	
	public String convertirFechaNac(Date fechaNac) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(fechaNac);
	}
	
	public String primeraLetraMayuscula(String input) {
	    if (input == null || input.isEmpty()) {
	        return input;
	    }

	    String primerCaracter = input.substring(0, 1).toUpperCase();
	    String resto = input.substring(1).toLowerCase();

	    return primerCaracter + resto;
	}
	
	public void limpiarCampos() {
		usuario = new UsuarioDTO();
		passwordNueva = "";
	}
	
	

}
