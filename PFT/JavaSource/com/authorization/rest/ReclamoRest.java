package com.authorization.rest;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dto.ReclamoDTO;
import com.entities.EstadoSolicitud;
import com.entities.Evento;
import com.entities.Reclamo;
import com.entities.Semestre;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.repositories.ReclamoRepository;
import com.services.EstadoSolicitudService;
import com.services.EventoService;
import com.services.ReclamoService;
import com.services.SemestreService;
import com.utils.JwtUtil;
import com.validation.ValidationError;


@Path("/reclamos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReclamoRest {
	
	@EJB private ReclamoRepository dao;
	
	@Inject private ReclamoService reclamoService;
	@Inject private EventoService eventoService;
	@Inject private SemestreService semestreService;
	@Inject private EstadoSolicitudService estadoService;
	
    @GET
    @Path("/")
    public Response obtenerTodosLosReclamos(@HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	return Response.ok(dao.getAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerReclamoPorId(@PathParam("id") Long id, @HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	Reclamo rec = dao.get(id);
    	
    	if (rec == null) {
    		return Response.status(Response.Status.NOT_FOUND).build();
		}
    	
        return Response.ok(rec).build();
    }

    @POST
    public Response crearNuevoReclamo(ReclamoDTOrest reclamo, @HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
        try {
        	ReclamoDTO dtoNormal = new ReclamoDTO();
        	dtoNormal.setTitulo(reclamo.getTitulo());
        	dtoNormal.setDetalle(reclamo.getDetalle());
        	dtoNormal.setTipoReclamo(reclamo.getTipoReclamo());
        	
        	Evento eve = eventoService.findById(reclamo.getIdEvento());
        	dtoNormal.setEvento(eve);
        	
        	Semestre sem = semestreService.buscarPorId(reclamo.getIdSemestre());
        	dtoNormal.setSemestre(sem);
        	
        	EstadoSolicitud est = estadoService.get(reclamo.getIdEstado());
        	dtoNormal.setEstado(est);
        	ValidationError.validarReclamo(dtoNormal);
        	dao.save(reclamoService.dtoRestToReclamo(reclamo));
        	return Response.status(Response.Status.CREATED).entity(reclamo).build();
        	
		} catch (ServiciosException e) {
			
			String errorMessage = e.getMessage();
		    return Response.status(Response.Status.BAD_REQUEST)
		            .entity(errorMessage).build();
		    
		} catch (FieldValidationException e) {
			
			
			String errorMessage = e.getMessage();
		    return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		    
		}
        
    }

    @PUT
    @Path("/")
    public Response actualizarReclamo(ReclamoDTOrest reclamo, @HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
        
        try {
        	ReclamoDTO dtoNormal = new ReclamoDTO();
        	dtoNormal.setTitulo(reclamo.getTitulo());
        	dtoNormal.setDetalle(reclamo.getDetalle());
        	dtoNormal.setTipoReclamo(reclamo.getTipoReclamo());
        	
        	Evento eve = eventoService.findById(reclamo.getIdEvento());
        	dtoNormal.setEvento(eve);
        	
        	Semestre sem = semestreService.buscarPorId(reclamo.getIdSemestre());
        	dtoNormal.setSemestre(sem);
        	
        	EstadoSolicitud est = estadoService.get(reclamo.getIdEstado());
        	dtoNormal.setEstado(est);
        	ValidationError.validarReclamo(dtoNormal);
        	dao.update(reclamoService.dtoRestToReclamo(reclamo));
        	return Response.status(Response.Status.CREATED).entity(reclamo).build();
        	
		} catch (ServiciosException e) {
			
			String errorMessage = e.getMessage();
		    return Response.status(Response.Status.BAD_REQUEST)
		            .entity(errorMessage).build();
		    
		} catch (FieldValidationException e) {
			
			
			String errorMessage = e.getMessage();
		    return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		    
		}
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarReclamo(@PathParam("id") Long id, @HeaderParam("Authorization") String authorizationHeader) {    	
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
//    	
    	try {
			dao.delete(dao.get(id));
			return Response.status(Response.Status.OK).build();
		} catch (ServiciosException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
    }
    
    
}
	
