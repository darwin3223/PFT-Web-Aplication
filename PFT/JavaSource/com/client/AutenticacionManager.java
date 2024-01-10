package com.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.entities.Usuario;
import com.enums.TipoUsuario;
import com.filtros.PermissionChecker;
import com.services.UsuarioService;
import com.utils.CookiesUtil;
import com.utils.JwtUtil;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionAutenticacion")
public class AutenticacionManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombreUsuarioLogin;
	private String contraseniaLogin;

	private String tipoUsuarioLog;
	
	@Inject	private UsuarioService usuarioService;
	
	@Inject	private UsuarioManager gestionUsuario;
	@Inject	private PermissionChecker permissionChecker;
	
	@Inject private NavbarManager navbar;

	@PostConstruct
	public void init() {}
	
	public String login() {
		Usuario user = usuarioService.login(nombreUsuarioLogin, contraseniaLogin);
		if (user != null) {
			navbar.updateNavbar(user.getTipoUsuario());
			navbar.setNombreUsuario(user.getNombre()+" "+user.getApellido());
			limpiarCamposLogin();
			return "menuPrincipal"; // Outcome 'menuPrincipal' configurado en faces-config.xml
		}
		return null;
	}

	public String logout() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
	    session.invalidate();
		CookiesUtil.setCookie("jwt", "", 0);
		return "logout";
	}
	
	public String loadNombreUsuarioLogeado() {
		String jwt = CookiesUtil.getCookie("jwt");
		return JwtUtil.getNombreUsuarioFromJwt(jwt);
	}
	
	public long loadIdUsuarioLogeado() {
		String jwt = CookiesUtil.getCookie("jwt");
		return JwtUtil.getIdFromJwt(jwt);
	}
	
	public TipoUsuario loadTipoUsuarioLogeado() {
		String jwt = CookiesUtil.getCookie("jwt");
		return JwtUtil.getTipoUsuarioFromJwt(jwt);
	}
	
	public void limpiarCamposLogin() {
		nombreUsuarioLogin = "";
		contraseniaLogin = "";
	}

}
