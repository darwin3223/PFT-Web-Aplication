package com.client;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Random;

import org.omnifaces.util.Ajax;
import org.primefaces.PrimeFaces;

import com.dto.EstudianteDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Departamento;
import com.entities.Estudiante;
import com.entities.Itr;
import com.entities.Localidad;
import com.entities.Usuario;
import com.enums.EstadoEstudiante;
import com.enums.EstadoUsuario;
import com.enums.RolTutor;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.exceptions.FieldValidationException;
import com.exceptions.MailException;
import com.exceptions.ServiciosException;
import com.services.AnalistaService;
import com.services.DepartamentoService;
import com.services.EstudianteService;
import com.services.ItrService;
import com.services.LocalidadService;
import com.services.TutorService;
import com.services.UsuarioService;
import com.utils.MailUtil;
import com.utils.MessagesUtil;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@SessionScoped
@Named(value = "gestionUsuario")
public class UsuarioManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private UsuarioService usuarioService;
	@Inject private ItrService itrService;
	@Inject private LocalidadService localidadService;
	@Inject	private AnalistaService analistaService;
	@Inject	private EstudianteService estudianteService;
	@Inject	private TutorService tutorService;
	@Inject	private DepartamentoService departamentoService;

	private UsuarioDTO usuarioSeleccionado = UsuarioDTO.builder().build();
	private EstudianteDTO estudianteSeleccionado = new EstudianteDTO();
	private TutorDTO tutorSeleccionado = new TutorDTO();
	
	private String nombreDepartamentoSeleccionado;
	private String nombreRolTutorSeleccionado;
	private long idLocalidadSeleccionada;
	private Long idItr;
	private long idDepartamentoSeleccionado;
	private String nombreLocalidadSeleccionada;
	private String tipoUsuarioSeleccionado;
	private long idItrSeleccionado;
	
	private Usuario usuarioRecuperar;
	private String codigoGenerado;
	private String recuperarPasswordMail;
	private String recuperarPasswordCodigo;
	private String newPassword;
	private boolean recuperarPasswordMailBool = true;
	private boolean recuperarPasswordCodigoBool = false;
	private boolean newPasswordBool = false;
	
	private boolean renderInputEstudiante;
	private boolean renderInputTutor;
	private boolean renderInputText;
	
	private boolean verComboLocalidades = false;
	
	List<Localidad> listaLocalidades = new LinkedList<>();
	List<String> listaNombreLocalidades;
	
	private int pruebaAño;

	private LocalDate fechaActual = LocalDate.now();
	
	private String yearRange = (fechaActual.getYear() - 100) + ":" + (fechaActual.getYear() + 10);

	public void registrarUsuario() {
		TipoUsuario tipoAsignado =
				TipoUsuario.ANALISTA.getNombre().equals(tipoUsuarioSeleccionado) ? TipoUsuario.ANALISTA :
				TipoUsuario.TUTOR.getNombre().equals(tipoUsuarioSeleccionado) ? TipoUsuario.TUTOR :
				TipoUsuario.ESTUDIANTE.getNombre().equals(tipoUsuarioSeleccionado) ? TipoUsuario.ESTUDIANTE :
					null;
		
		usuarioSeleccionado.setTipoUsuario(tipoAsignado);
		usuarioSeleccionado.setItr(itrService.get(idItrSeleccionado));
		usuarioSeleccionado.setVerificacion(Verificacion.NO_VERIFICADO);
		usuarioSeleccionado.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuarioSeleccionado.setDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		estudianteSeleccionado.setEstadoEstudiante(EstadoEstudiante.MATRICULADO);
		usuarioSeleccionado.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		
		if(usuarioSeleccionado.getMailInstitucional().contains("@")) {
			String[] partes = usuarioSeleccionado.getMailInstitucional().split("@");
			usuarioSeleccionado.setNombreUsuario(partes[0]);
		}
		
		RolTutor rolAsignado = RolTutor.ENCARGADO.getNombre().equals(nombreRolTutorSeleccionado)
				? RolTutor.ENCARGADO
				: RolTutor.Tutor.getNombre().equals(nombreRolTutorSeleccionado)? RolTutor.Tutor
				: null;
		tutorSeleccionado.setRolTutor(rolAsignado);
		
		try {
			if (TipoUsuario.ESTUDIANTE.equals(usuarioSeleccionado.getTipoUsuario())) {
				estudianteService.saveEstudiante(usuarioSeleccionado, estudianteSeleccionado);
				MessagesUtil.createInfoNotification("Registro de estudiante correcto", "El estudiante se registró correctamente, este usuario debe ser verificado para poder logearse.");
			} else if(TipoUsuario.TUTOR.equals(usuarioSeleccionado.getTipoUsuario())) {
				tutorService.saveTutor(usuarioSeleccionado, tutorSeleccionado);
				MessagesUtil.createInfoNotification("Registro de tutor correcto", "Se registró correctamente el usuario Tutor, debe ser verificado para poder logearse.");
			} else {
				analistaService.saveAnalista(usuarioSeleccionado);
				MessagesUtil.createInfoMessage("Registro de analista correcto", "El analista se registró correctamente, este usuario debe ser verificado para poder logearse.");
			}
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorMessage("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorMessage("Error al registrar usuario", "El usuario no se pudo registrar, Asegúrate de que los datos ingresados son correctos o inténtalo más tarde.");
		}
	}
	
	public List<Itr> cargarListaItr() {
		return itrService.getAll();
	}
	
	public List<Departamento> cargarListaDepartamento(){
		return departamentoService.obtenerTodosDepartamento();
	}
	
	public void cargarListaLocalidades() {
		listaLocalidades = localidadService.obtenerLocalidadesPorDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		if (listaLocalidades.size() > 0) {	
			verComboLocalidades = true;
		} else {
			listaLocalidades = new ArrayList<>();
			verComboLocalidades = false;
		}
	}

	public void menuItemChanged(String tipoUsuario) {
		if ("Estudiante".equals(tipoUsuario)) {
			renderInputEstudiante = true;
			renderInputTutor = false;
		} else if ("Tutor".equals(tipoUsuario)) {
			renderInputEstudiante = false;
			renderInputTutor = true;
		} else {
			renderInputEstudiante = false;
			renderInputTutor = false;
			nombreRolTutorSeleccionado = null;
		}
	}
	
	public List<Integer> cargarListaAños() {
		List<Integer> listaAños = new LinkedList<>();

		for (int year = fechaActual.getYear() - 30; year <= fechaActual.getYear() + 10; year++) {
			listaAños.add(year);
        }
		return listaAños;
	}
	
	public void showMessage(FacesMessage message) {
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void infoMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void recibirMail() {
		if (comprobarMail()) {
			String asunto = "Codigo de recuperación de contraseña";
			String mensaje = "El codigo para recuperar la contraseña es el siguiente: "+generarCodigo();
			try {
				MailUtil.mandarMail(recuperarPasswordMail, mensaje, asunto);
				MessagesUtil.createInfoNotification("Exito!","Enviamos a tu correo el codigo para continuar.");
	            Ajax.update("growl");
	            recuperarPasswordMailBool = false;
				recuperarPasswordCodigoBool = true;
				Ajax.update("formRegistro");
			} catch (MailException e) {
				MessagesUtil.createErrorNotification("Error!",e.getMessage());
	        	Ajax.update("growl");
			}
		}
	}
	
	public void recibirCodigo() {
		if (recuperarPasswordCodigo.equals(codigoGenerado)) {
			MessagesUtil.createInfoNotification("Exito!", "Codigo ingresado correcto.");
            Ajax.update("growl");

            recuperarPasswordCodigoBool = false;
			newPasswordBool = true;
			Ajax.update("formRegistro");
		} else {
			MessagesUtil.createErrorNotification("Error!", "Codigo ingresado incorrecto.");
            Ajax.update("growl");
		}
	}
	
	public void cargarLostPassword() {
		recuperarPasswordMail = "";
		recuperarPasswordCodigo = "";
		newPassword = "";
		recuperarPasswordMailBool = true;
		recuperarPasswordCodigoBool = false;
		newPasswordBool = false;
	}
	
	public boolean comprobarMail(){
		if ((recuperarPasswordMail.contains("@") && recuperarPasswordMail.endsWith("utec.edu.uy"))
                && (recuperarPasswordMail.length() >= 1 && recuperarPasswordMail.length() <= 320)) {
            usuarioRecuperar = usuarioService.getUsuarioByMailInstitucional(recuperarPasswordMail);
            return true;
        }
		MessagesUtil.createErrorNotification("Error!",
                "El correo debe contener un @, el dominio utec.edu.uy y entre 1 y 320 dígitos.");
        Ajax.update("growl");
		return false;
	}
	
	public String generarCodigo() {
		int longitudCodigo = 6; // Longitud deseada del código
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Caracteres permitidos en el código
        Random random = new Random();
        StringBuilder codigoAleatorio = new StringBuilder();

        for (int i = 0; i < longitudCodigo; i++) {
            int indice = random.nextInt(caracteres.length());
            char caracterAleatorio = caracteres.charAt(indice);
            codigoAleatorio.append(caracterAleatorio);
        }
        
        codigoGenerado = codigoAleatorio.toString();
        return codigoAleatorio.toString();
	}
	
	public String cambiarContraseña() {
		try {
			usuarioService.updatePassword(usuarioRecuperar.getIdUsuario(), newPassword);
			MessagesUtil.createInfoNotification("Exito!", "Contraseña cambiada correctamente");
			Ajax.update("growl");
			return "login";
		} catch (FieldValidationException e) {
			MessagesUtil.createInfoMessage("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createInfoMessage("Error al cambiar la contraseña", e.getMessage());
		}
		return "";
	}
	
	public List<Integer> cargarFiltroGen() {
		List<Estudiante> listaEstudiantes = estudianteService.getEstudiantes();
		List<Integer> listaGeneraciones = new LinkedList<>();
		
		for (Estudiante est : listaEstudiantes) {
			if (!(listaGeneraciones.contains(est.getAñoIngreso()))) {
				listaGeneraciones.add(est.getAñoIngreso());
			}
			
		}
		
		return listaGeneraciones;
	}
}
