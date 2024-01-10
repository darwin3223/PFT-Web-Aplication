package com.validation;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.dto.AccionJustificacionDTO;
import com.dto.AccionReclamoDTO;
import com.dto.EstadoSolicitudDTO;
import com.dto.EstudianteDTO;
import com.dto.ItrDTO;
import com.dto.JustificacionDTO;
import com.dto.ReclamoDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Itr;
import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.enums.Verificacion;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.services.ItrService;
import com.utils.HashingUtil;
import com.utils.MessagesUtil;

public class ValidationError {

	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%^&*()\\-_=+\\[\\]{}|;:'\",<.>/?`~])[A-Za-z\\d!@#\\$%^&*()\\-_=+\\[\\]{}|;:'\",<.>/?`~]{8,256}$";
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

	public static boolean validateLogin(Usuario user, String username, String password) {
		if (user == null) {
			MessagesUtil.createWarnMessage("Usuario no válido", "El usuario ingresado no existe en el sistema.");
			return false;
		} else if (!username.equals(user.getNombreUsuario()) || !HashingUtil.verify(password, user.getContrasenia())) {
			MessagesUtil.createWarnMessage("Credenciales no válidas", "Los datos ingresados no son válidos. Acceso denegado.");
			return false;
		} else if (Verificacion.VERIFICADO != user.getVerificacion()) {
			MessagesUtil.createWarnMessage("Usuario no autorizado", "El usuario todavia no esta verificado, espere a que un Analista lo verifique");
			return false;
		} else if (EstadoUsuario.ACTIVO != user.getEstadoUsuario()) {
			MessagesUtil.createWarnMessage("Usuario no esta Activo", "El usuario no tiene el estado Activo, no puede ingresar a la aplicación");
			return false;
		}
		return true;
	}

	// Validaciones nuevas
	
	public static void validarUsuario(UsuarioDTO dto) throws FieldValidationException {
		if (dto.getNombre().isEmpty() || !(dto.getNombre().length() >= 1 && dto.getNombre().length() <= 50))
			throw new FieldValidationException("El nombre debe contener entre 1 y 50 dígitos.");

        if (dto.getApellido().isEmpty() || !(dto.getApellido().length() >= 1 && dto.getApellido().length() <= 50))
        	throw new FieldValidationException("El apellido debe contener entre 1 y 50 dígitos.");
        
        validarDocumento(dto.getDocumento());
        
        validarFechaNacimiento(dto.getFechaNacimiento());
        
        validarCorreo(dto.getMail());
        
        if (!(dto.getMailInstitucional().contains("@") && dto.getMailInstitucional().endsWith("utec.edu.uy")) || !(dto.getMailInstitucional().length() >= 1 && dto.getMailInstitucional().length() <= 320))
        	throw new FieldValidationException("El mail institucional debe contener un @, el dominio utec.edu.uy y entre 1 y 320 dígitos.");
        
        if (dto.getTelefono().isEmpty() || !(dto.getTelefono().length() == 9))
        	throw new FieldValidationException("El número de teléfono debe contener 9 dígitos.");

        if (dto.getGenero() == null)
        	throw new FieldValidationException("El genero no puede estar vacío.");

        if (dto.getTipoUsuario() == null)
        	throw new FieldValidationException("El tipo de usuario no puede estar vacío.");

        if (dto.getItr() == null)
        	throw new FieldValidationException("El Itr no puede estar vacío.");

        if (dto.getLocalidad() == null)
        	throw new FieldValidationException("La localidad no puede estar vacía.");

        if (!PASSWORD_PATTERN.matcher(dto.getContrasenia()).matches())
        	throw new FieldValidationException("La contraseña debe contener al menos una letra mayúscula, al menos un dígito y al menos un carácter especial (cualquier carácter de puntuación o símbolo). La longitud de la contraseña debe ser de al menos 8 y un maximo de 256 caracteres.");

        if (dto.getEstadoUsuario() == null)
        	throw new FieldValidationException("El estado de usuario no puede estar vacío.");

        if (dto.getVerificacion() == null)
        	throw new FieldValidationException("La verificación no puede estar vacía.");
        
        
	}
	
	public static void validarTutor(TutorDTO dto) throws FieldValidationException {
		if (dto.getArea() == null) throw new FieldValidationException("El área no debe estar vacía.");
		if (dto.getRolTutor() == null) throw new FieldValidationException("El rol no puede estar vacío.");
	}
	
	public static void validarEstudiante(EstudianteDTO dto) throws FieldValidationException {
		if (dto.getEstadoEstudiante() == null) throw new FieldValidationException("El estado del estudiante no puede estar vacío.");
        if (dto.getAñoIngreso() == 0) throw new FieldValidationException("El año de ingreso no puede estar vacío.");
	}
	
	public static void validarAccionReclamo(AccionReclamoDTO dto) throws FieldValidationException {
		if (dto.getDetalle().isEmpty()) throw new FieldValidationException("El detalle de la acción del reclamo no puede estar vacío.");
		
		// Esta validación es redundante con la primera, pero la dejo por si llegamos a aumentar el mínimo de caracteres
		if (dto.getDetalle().length() < 1) throw new FieldValidationException("El detalle de la acción del reclamo debe contener al menos 1 caracter.");
		if (dto.getDetalle().length() > 200) throw new FieldValidationException("El detalle de la acción del reclamo no debe contener más de 200 caracteres.");
	}
	
	public static void validarReclamo(ReclamoDTO dto) throws FieldValidationException {
        if (dto.getTitulo() == null || !(dto.getTitulo().length() >= 1 && dto.getTitulo().length() <= 50))
            throw new FieldValidationException("El título debe contener entre 1 y 50 dígitos.");
        
        if (dto.getDetalle() == null || !(dto.getDetalle().length() >= 1 && dto.getDetalle().length() <= 200))
        	throw new FieldValidationException("El detalle debe contener entre 1 y 200 dígitos.");

        if (dto.getEvento() == null)
        	throw new FieldValidationException("El Evento no puede estar vacío.");
        
        if (dto.getEstado() == null)
        	throw new FieldValidationException("El Estado no puede estar vacío.");
        
        if (dto.getSemestre() == null)
        	throw new FieldValidationException("El Semestre no puede estar vacío.");
        
        if (dto.getTipoReclamo() == null)
        	throw new FieldValidationException("El Tipo de Reclamo no puede estar vacío.");
    }
	
	public static void validarItr(ItrDTO dto, ItrService itrService) throws FieldValidationException, ServiciosException {
		if (dto.getNombre().isEmpty()) throw new FieldValidationException("El nombre del ITR no puede estar vacío.");
		// Esta validación es redundante con la primera, pero la dejo por si llegamos a aumentar el mínimo de caracteres
		
		Itr itrPorNombre = itrService.findByName(dto.getNombre());
		if (itrPorNombre != null && dto.getIdItr() != dto.getIdItr()) {
			throw new FieldValidationException("El Itr que intenta ingresar ya existe.");
		}
		
		if (dto.getNombre().length() < 1) throw new FieldValidationException("El nombre del ITR debe contener al menos 1 caracter.");
		if (dto.getNombre().length() > 30) throw new FieldValidationException("El nombre del ITR no debe contener más de 30 caracteres.");

		if (dto.getDepartamento() == null) throw new FieldValidationException("El departamento del ITR no puede estar vacío.");
		if (dto.getLocalidad() == null) throw new FieldValidationException("La localidad del ITR no puede estar vacío.");
		if (dto.getEstado() == null) throw new FieldValidationException("El estado del ITR no puede estar vacío.");
	}
	
	public static void validarEstado(EstadoSolicitudDTO dto) throws FieldValidationException {
		if (dto.getNombre().isEmpty()) throw new FieldValidationException("El nombre del Estado no puede estar vacío.");
		
		// Esta validación es redundante con la primera, pero la dejo por si llegamos a aumentar el mínimo de caracteres
		if (dto.getNombre().length() < 1) throw new FieldValidationException("El nombre del Estado debe contener al menos 1 caracter.");
		if (dto.getNombre().length() > 30) throw new FieldValidationException("El nombre del Estado no debe contener más de 30 caracteres.");
	}
	
	public static void validarJustificacion(JustificacionDTO dto) throws FieldValidationException {
		if (dto.getDetalle().isEmpty()) throw new FieldValidationException("El detalle de la justificación no puede estar vacío.");
		
		// Esta validación es redundante con la primera, pero la dejo por si llegamos a aumentar el mínimo de caracteres
		if (dto.getDetalle().length() < 1) throw new FieldValidationException("El detalle de la justificación debe contener al menos 1 caracter.");
		if (dto.getDetalle().length() > 200) throw new FieldValidationException("El detalle de la justificación no debe contener más de 200 caracteres.");
	}
	
	public static void validarAccionJustificacion(AccionJustificacionDTO dto) throws FieldValidationException {
		if (dto.getDetalle().isEmpty()) throw new FieldValidationException("El detalle de la acción de justificación no puede estar vacío.");
		
		// Esta validación es redundante con la primera, pero la dejo por si llegamos a aumentar el mínimo de caracteres
		if (dto.getDetalle().length() < 1) throw new FieldValidationException("El detalle de la acción de justificación debe contener al menos 1 caracter.");
		if (dto.getDetalle().length() > 200) throw new FieldValidationException("El detalle de la acción de justificación no debe contener más de 200 caracteres.");
	}

	public static void validarClave(String clave) throws FieldValidationException {
		if (!(PASSWORD_PATTERN.matcher(clave).matches()))
            throw new FieldValidationException("La contraseña debe contener al menos una letra mayúscula, al menos un dígito y al menos un carácter especial (cualquier carácter de puntuación o símbolo). La longitud de la contraseña debe ser de al menos 8 caracteres con un maximo de 256.");
	}
	
	private static void validarDocumento(String documento) throws FieldValidationException {
		if (documento.isEmpty())
        	throw new FieldValidationException("El documento no puede estar vacío.");
        
        if (documento.length() >= 9)
        	throw new FieldValidationException("El documento no puede tener más de 8 caracteres.");
		
		if (documento.length() == 7) {
			documento = "0" + documento;
		}

		int ultimoDigito = documento.charAt(documento.length() - 1);
		int digitoVerificar = Character.getNumericValue(ultimoDigito);

		String codigoMultiplicador = "2987634";
		int acumulador = 0;

		for (int i = 0; i < documento.length() - 1; i++) {
			acumulador += Character.getNumericValue(documento.charAt(i)) * Character.getNumericValue(codigoMultiplicador.charAt(i));
		}

		int digitoVerificador = (10 - (acumulador % 10)) % 10;
		
		if (digitoVerificar != digitoVerificador) throw new FieldValidationException("El documento no es válido");
	}

	private static void validarCorreo(String mail) throws FieldValidationException {
		if ((mail.length() <= 1) || (mail.length() > 320))
			throw new FieldValidationException("El correo electrónico debe contener entre 1 y 320 dígitos.");
		
		// Expresión regular para verificar el formato del correo electrónico
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail);

		if (!matcher.matches()) {
			throw new FieldValidationException("El formato del correo electrónico no es válido");
		}

		// Lista de dominios de correo electrónico comunes
		List<String> dominiosComunes = Arrays.asList("gmail.com", "yahoo.com", "hotmail.com", "outlook.com");

		// Obtener el dominio del correo electrónico
		String[] partes = mail.split("@");
		if (partes.length != 2) {
			throw new FieldValidationException("El dominio de tú correo electrónico no es válido, debe contener un @.");
		}

		String dominio = partes[1];

		if (!dominiosComunes.contains(dominio.toLowerCase()))
			throw new FieldValidationException("El dominio de tú correo electrónico no pertenece a la lista de dominios comunes. uno de los siguientes dominios: gmail.com, yahoo.com, hotmail.com, outlook.com");
	}

	private static void validarFechaNacimiento(Date fecha) throws FieldValidationException {
		if (fecha == null)
			throw new FieldValidationException("La fecha de nacimiento no puede estar vacía.");
		
        LocalDate fechaNacimiento = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period periodo = Period.between(fechaNacimiento, LocalDate.now());
        
        int edad = periodo.getYears();
        
        if (!LocalDate.now().isAfter(fechaNacimiento))
        	throw new FieldValidationException("La fecha de nacimiento no puede ser mayor a la fecha actual.");
        
        if (edad < 18)
        	throw new FieldValidationException("Según la fecha de nacimiento registrada, no eres mayor a 18 años.");
	}
}
