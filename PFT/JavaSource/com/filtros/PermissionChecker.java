package com.filtros;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.enums.TipoUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
@Named(value = "permissionChecker")
public class PermissionChecker {

	private HashMap<String, List<TipoUsuario>> map = new HashMap<String, List<TipoUsuario>>();;
	
	@PostConstruct
	public void init() {
		new WebPermission(map, "/profile").addAll();
		new WebPermission(map, "/welcome").addAll();
		new WebPermission(map, "/itrs").addAnalista();
		new WebPermission(map, "/estados").addAnalista();
		new WebPermission(map, "/usuarios").addAnalista();
		new WebPermission(map, "/reportes/reclamos").addTutor().addAnalista();
		new WebPermission(map, "/convocatorias").addAll();
		new WebPermission(map, "/justificaciones").addEstudiante().addAnalista();
		new WebPermission(map, "/reclamos").addEstudiante().addAnalista();
	}
	
	public boolean check(String path, TipoUsuario t) throws Exception {
		List<TipoUsuario> allowed = map.get(path);
		if (allowed == null) throw new Exception("No existe la ruta ingresada");
		return allowed.contains(t);
	}
}
