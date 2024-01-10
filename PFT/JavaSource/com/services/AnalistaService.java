package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dto.UsuarioDTO;
import com.entities.Analista;
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
public class AnalistaService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="AnalistaDAO")
	private Repository<Analista> dao;
	
	public Analista getAnalistaById(long id) {
		return dao.get(id);
	}
	
	public List<Analista> getAnalistas(){
		return dao.getAll();
	}
	
	public Analista saveAnalista(UsuarioDTO usuDTO) throws FieldValidationException, ServiciosException {
		ValidationError.validarUsuario(usuDTO);
		Analista analista = dtoToAnalista(usuDTO);
		analista.setContrasenia(HashingUtil.hash(analista.getContrasenia()));
		dao.save(analista);
		return analista;
	}
	
	public Analista updateAnalista(UsuarioDTO usuDTO) throws FieldValidationException, ServiciosException {
		ValidationError.validarUsuario(usuDTO);
		usuDTO.setNombreUsuario(null);
		
		String[] partes = usuDTO.getMailInstitucional().split("@");
		usuDTO.setNombreUsuario(partes[0]);
		
		Analista analista = dtoToAnalista(usuDTO);
		dao.update(analista);
		return analista;
	}
	
	public Analista dtoToAnalista(UsuarioDTO usuDTO) {
		Analista analista = new Analista();
		
		analista.setIdUsuario(usuDTO.getIdUsuario());
		analista.setNombre(usuDTO.getNombre());
		analista.setApellido(usuDTO.getApellido());
		analista.setDocumento(Integer.parseInt(usuDTO.getDocumento()));
		analista.setFechaNacimiento(usuDTO.getFechaNacimiento());
		analista.setNombreUsuario(usuDTO.getNombreUsuario());
		analista.setContrasenia(usuDTO.getContrasenia());
		analista.setTipoUsuario(usuDTO.getTipoUsuario());
		analista.setVerificacion(usuDTO.getVerificacion());
		analista.setMail(usuDTO.getMail());
		analista.setMailInstitucional(usuDTO.getMailInstitucional());
		analista.setTelefono(usuDTO.getTelefono());
		analista.setGenero(usuDTO.getGenero());
		analista.setEstadoUsuario(usuDTO.getEstadoUsuario());
		analista.setItr(usuDTO.getItr());
		analista.setLocalidad(usuDTO.getLocalidad());

		return analista;
	}
	
}
