package com.services;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dto.EstudianteDTO;
import com.dto.UsuarioDTO;
import com.entities.Estudiante;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.repositories.Repository;
import com.utils.HashingUtil;
import com.validation.ValidationError;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@Stateless
@LocalBean
public class EstudianteService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="EstudianteDAO")
	private Repository<Estudiante> dao;

	public Estudiante getEstudianteById(long id) {
		return dao.get(id);
	}
	
	public List<Estudiante> getEstudiantes(){
		return dao.getAll();
	}
	
	public Estudiante saveEstudiante(UsuarioDTO usuDTO, EstudianteDTO estDTO) throws FieldValidationException, ServiciosException {
		ValidationError.validarUsuario(usuDTO);
		ValidationError.validarEstudiante(estDTO);
		String[] partes = usuDTO.getMailInstitucional().split("@");
		usuDTO.setNombreUsuario(partes[0]);
		Estudiante estudiate = dtoToEstudiante(usuDTO, estDTO);
		estudiate.setContrasenia(HashingUtil.hash(estudiate.getContrasenia()));
		dao.save(estudiate);
		return estudiate;
	}
	
	public Estudiante updateEstudiante(UsuarioDTO usuDTO, EstudianteDTO estDTO) throws FieldValidationException, ServiciosException {
		ValidationError.validarUsuario(usuDTO);
		ValidationError.validarEstudiante(estDTO);
		Estudiante estudiante = dtoToEstudiante(usuDTO, estDTO);
		dao.update(estudiante);
		return estudiante;
	}
	
	public Estudiante dtoToEstudiante(UsuarioDTO usuDTO,EstudianteDTO estDTO) {
		Estudiante estudiante = new Estudiante();
		
		estudiante.setIdUsuario(usuDTO.getIdUsuario());
		estudiante.setNombre(usuDTO.getNombre());
		estudiante.setApellido(usuDTO.getApellido());
		estudiante.setDocumento(Integer.parseInt(usuDTO.getDocumento()));
		estudiante.setFechaNacimiento(usuDTO.getFechaNacimiento());
		estudiante.setNombreUsuario(usuDTO.getNombreUsuario());
		estudiante.setContrasenia(usuDTO.getContrasenia());
		estudiante.setTipoUsuario(usuDTO.getTipoUsuario());
		estudiante.setVerificacion(usuDTO.getVerificacion());
		estudiante.setMail(usuDTO.getMail());
		estudiante.setMailInstitucional(usuDTO.getMailInstitucional());
		estudiante.setTelefono(usuDTO.getTelefono());
		estudiante.setGenero(usuDTO.getGenero());
		estudiante.setEstadoUsuario(usuDTO.getEstadoUsuario());
		estudiante.setItr(usuDTO.getItr());
		estudiante.setLocalidad(usuDTO.getLocalidad());
		
		estudiante.setEstadoEstudiante(estDTO.getEstadoEstudiante());
		estudiante.setAñoIngreso(estDTO.getAñoIngreso());

		return estudiante;
	}
	
	public EstudianteDTO estudianteToDTO(Estudiante e) {
		EstudianteDTO dto = new EstudianteDTO();
		dto.setEstadoEstudiante(e.getEstadoEstudiante());
		dto.setAñoIngreso(e.getAñoIngreso());
		return dto;
	}
}
