package com.converters;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import com.entities.Localidad;
import com.services.LocalidadService;

@Named(value = "localidadConverter")
@SessionScoped
@FacesConverter("LocalidadConverter")
public class LocalidadConverter implements Serializable, Converter<Object> {

	@EJB
	private LocalidadService localidadService;
	private static final long serialVersionUID = 1L;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        Long codigoLocalidad = Long.valueOf(value);
        
        Localidad localidadDTO = localidadService.buscarLocalidadPorId(codigoLocalidad);

        return localidadDTO;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || !(value instanceof Localidad)) {
            return null;
        }

        Localidad localidadDTO = (Localidad) value;
        
        return String.valueOf(localidadDTO.getIdLocalidad());
    }	

}
