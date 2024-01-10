package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dto.UsuarioDTO;
import com.entities.Usuario;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.repositories.UsuarioRepository;
import com.utils.CookiesUtil;
import com.utils.HashingUtil;
import com.utils.JwtUtil;
import com.validation.ValidationError;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class UsuarioService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB private UsuarioRepository dao;
	
	public List<Usuario> getUsuarios(){
		return dao.getAll();
	}
	
	public Usuario getUsuarioById(long id) {
		return dao.get(id);
	}
	
	public void updatePassword(long id, String pass) throws FieldValidationException, ServiciosException {
		ValidationError.validarClave(pass);	
		dao.changePassword(id, HashingUtil.hash(pass));
	}
	
	public void deleteUsuarioLogical(long id) throws ServiciosException {
		dao.deleteLogical(id);
	}
	
	public void deleteUsuario(long id) throws ServiciosException {
		// dao.delete(dao.get(id));
	}
	
	public Usuario getUsuarioByNombreUsuario(String nombreUsuario) {
		return dao.getByNombreUsuario(nombreUsuario);
	}
	
	public Usuario getUsuarioByMailInstitucional(String mail) {
		return dao.getByMailInstitucional(mail);
	}
	
	public Usuario getUsuarioByDocumento(String documento) {
		return dao.getByDocumento(Integer.parseInt(documento));
	}
	
	public Usuario login(String username, String password) {
		Usuario usuario = dao.getByNombreUsuario(username);
		
		if (ValidationError.validateLogin(usuario, username, password)) {
			String token = JwtUtil.createJwt(username, usuario.getTipoUsuario(), usuario.getIdUsuario());
			CookiesUtil.setCookie("jwt", token, 480 * 60);
			return usuario;
		} else {
			return null;
		}
	}
	
	public UsuarioDTO convertUsuariotoDto(Usuario usuario) {
		return UsuarioDTO.builder()
				.nombre(usuario.getNombre())
				.apellido(usuario.getApellido())
				.documento(usuario.getDocumento() + "")
				.fechaNacimiento(usuario.getFechaNacimiento())
				.nombreUsuario(usuario.getNombreUsuario())
				.contrasenia(usuario.getContrasenia())
				.tipoUsuario(usuario.getTipoUsuario())
				.verificacion(usuario.getVerificacion())
				.mail(usuario.getMail())
				.mailInstitucional(usuario.getMailInstitucional())
				.telefono(usuario.getTelefono())
				.genero(usuario.getGenero())
				.estadoUsuario(usuario.getEstadoUsuario())
				.itr(usuario.getItr())
				.localidad(usuario.getLocalidad())
				.departamento(usuario.getLocalidad().getDepartamento())
				.build();
	}
	
	public Usuario convertDtotoUsuario(UsuarioDTO usuario) {
		return Usuario.builder()
				.nombre(usuario.getNombre())
				.apellido(usuario.getApellido())
				.documento(Integer.parseInt(usuario.getDocumento()))
				.fechaNacimiento(usuario.getFechaNacimiento())
				.nombreUsuario(usuario.getNombreUsuario())
				.contrasenia(usuario.getContrasenia())
				.tipoUsuario(usuario.getTipoUsuario())
				.verificacion(usuario.getVerificacion())
				.mail(usuario.getMail())
				.mailInstitucional(usuario.getMailInstitucional())
				.telefono(usuario.getTelefono())
				.genero(usuario.getGenero())
				.estadoUsuario(usuario.getEstadoUsuario())
				.itr(usuario.getItr())
				.localidad(usuario.getLocalidad())
				.build();
	}
}