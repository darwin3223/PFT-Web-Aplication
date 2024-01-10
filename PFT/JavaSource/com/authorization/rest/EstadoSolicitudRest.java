package com.authorization.rest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
import com.services.EstadoSolicitudService;
import com.utils.JwtUtil;

@Path("/estadosSolicitud")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstadoSolicitudRest {
	
	@Inject EstadoSolicitudService estadoSolicitudService;	
    
	@GET
    @Path("/")
    public Response obtenerTodosLosEventos(@HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	return Response.ok(estadoSolicitudService.getAll()).build();
    }
}
	
