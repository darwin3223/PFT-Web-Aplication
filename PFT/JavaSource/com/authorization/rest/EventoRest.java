package com.authorization.rest;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.entities.Evento;
import com.repositories.Repository;
import com.utils.JwtUtil;

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoRest {
	
	@Inject
	@EJB(beanInterface = Repository.class, beanName="EventoDAO")
	private Repository<Evento> eventoDAO;	
    
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
    	
    	return Response.ok(eventoDAO.getAll()).build();
    }
}
	
