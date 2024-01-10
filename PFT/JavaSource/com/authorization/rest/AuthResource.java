package com.authorization.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.authorization.AuthHttpResponse;
import com.authorization.LoginRequest;
import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.enums.Verificacion;
import com.services.UsuarioService;
import com.utils.HashingUtil;
import com.utils.JwtUtil;

@Path("/auth")
public class AuthResource {

	@Inject
	private UsuarioService usuarioService;
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginRequest request) {
		try {
			Usuario usuario = usuarioService.getUsuarioByNombreUsuario(request.getUsername());
			
			if (usuario == null ) {
				String message = "Usuario no válido. El usuario ingresado no existe en el sistema.";
				return Response.status(Response.Status.NOT_FOUND).entity(message).build();
			}
			
			if (!HashingUtil.verify(request.getPassword(), usuario.getContrasenia())) {
				String message = "Credenciales no válidas. Los datos ingresados no son válidos, acceso denegado.";
				return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
			}
			
			if (Verificacion.VERIFICADO != usuario.getVerificacion()) {
				String message = "Usuario no autorizado. El usuario todavia no esta verificado, espere a que un Analista lo verifique";
				return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
			}
			
			if (EstadoUsuario.ACTIVO != usuario.getEstadoUsuario()) {
				String message = "El usuario no esta Activo. El usuario no tiene el estado Activo, no puede ingresar a la aplicación";
				return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
			}
			
			String token = JwtUtil.createJwt(usuario.getNombreUsuario(), usuario.getTipoUsuario(), usuario.getIdUsuario());
			
			return Response.ok(new AuthHttpResponse(token, usuario)).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
}
