package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Tutor;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.repositories.Repository;
import com.utils.HashingUtil;
import com.validation.ValidationError;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class TutorService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="TutorDAO")
	private Repository<Tutor> dao;

	public Tutor getTutorById(long id) {
		return dao.get(id);
	}
	
	public List<Tutor> getTutores(){
		return dao.getAll();
	}
	
	public Tutor saveTutor(UsuarioDTO usuDTO, TutorDTO tutDTO) throws FieldValidationException, ServiciosException {
		ValidationError.validarUsuario(usuDTO);
		ValidationError.validarTutor(tutDTO);
		String[] partes = usuDTO.getMailInstitucional().split("@");
		usuDTO.setNombreUsuario(partes[0]);
		Tutor tutor = dtoToTutor(usuDTO,tutDTO);
		tutor.setContrasenia(HashingUtil.hash(tutor.getContrasenia()));
		dao.save(tutor);
		return tutor;
	}
	
	public Tutor updateTutor(UsuarioDTO usuDTO, TutorDTO tutDTO) throws FieldValidationException, ServiciosException {
		ValidationError.validarUsuario(usuDTO);
		ValidationError.validarTutor(tutDTO);
		ValidationError.validarTutor(tutDTO);
		Tutor tutor = dtoToTutor(usuDTO,tutDTO);
		dao.update(tutor);
		return tutor;
	}
	
	public Tutor dtoToTutor(UsuarioDTO usuDTO, TutorDTO tutDTO) {
		Tutor tutor = new Tutor();
		
		tutor.setIdUsuario(usuDTO.getIdUsuario());
		tutor.setNombre(usuDTO.getNombre());
		tutor.setApellido(usuDTO.getApellido());
		tutor.setDocumento(Integer.parseInt(usuDTO.getDocumento()));
		tutor.setFechaNacimiento(usuDTO.getFechaNacimiento());
		tutor.setNombreUsuario(usuDTO.getNombreUsuario());
		tutor.setContrasenia(usuDTO.getContrasenia());
		tutor.setTipoUsuario(usuDTO.getTipoUsuario());
		tutor.setVerificacion(usuDTO.getVerificacion());
		tutor.setMail(usuDTO.getMail());
		tutor.setMailInstitucional(usuDTO.getMailInstitucional());
		tutor.setTelefono(usuDTO.getTelefono());
		tutor.setGenero(usuDTO.getGenero());
		tutor.setEstadoUsuario(usuDTO.getEstadoUsuario());
		tutor.setItr(usuDTO.getItr());
		tutor.setLocalidad(usuDTO.getLocalidad());
		
		tutor.setArea(tutDTO.getArea());
		tutor.setRolTutor(tutDTO.getRolTutor());

		return tutor;
	}
	
	public TutorDTO tutorToDTO(Tutor t) {
		TutorDTO dto = new TutorDTO();
		dto.setArea(t.getArea());
		dto.setRolTutor(t.getRolTutor());
		return dto;
	}
}
