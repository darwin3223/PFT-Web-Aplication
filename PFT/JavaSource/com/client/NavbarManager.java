package com.client;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.enums.TipoUsuario;
import com.utils.RenderVista;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "navbar")
public class NavbarManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private RenderVista render = new RenderVista(null);
	
	private String nombreUsuario;
	
	private String idMenuCarreras;
	private String idMenuUsuarios;
	private String idMenuItrs;
	private String idMenuPerfil;
	private String idMenuPrincipal;
	private String idMenuEventos;
	private String idMenuGestion;
	private String idMenuReportes;
	
	public void updateNavbar(TipoUsuario tipo) {
		render = new RenderVista(tipo);
	}
	
	public void idMenu(String menuSeleccionado) {
		
		if (menuSeleccionado.equals("menuItrs")) {
			idMenuItrs = "selected";
		}else {
			idMenuItrs = "menuItrs";
		}
		
		if (menuSeleccionado.equals("menuPerfil")) {
			idMenuPerfil= "selected";
		}else {
			idMenuPerfil = "menuPerfil";
		}
		
		if (menuSeleccionado.equals("menuUsuarios")) {
			idMenuUsuarios= "selected";
		}else {
			idMenuUsuarios = "menuUsuarios";
		}
		
		if (menuSeleccionado.equals("menuGestion")) {
			idMenuGestion= "selected";
		}else {
			idMenuGestion = "menuGestion";
		}
		
		if (menuSeleccionado.equals("menuPrincipal")) {
			idMenuPrincipal= "selected";
		}else {
			idMenuPrincipal = "menuPrincipal";
		}
		
		if (menuSeleccionado.equals("menuEventos")) {
			idMenuEventos= "selected";
		}else {
			idMenuEventos = "menuEventos";
		}
		
		if (menuSeleccionado.equals("menuReportes")) {
			idMenuReportes= "selected";
		}else {
			idMenuReportes = "menuReportes";
		}
	}
}
