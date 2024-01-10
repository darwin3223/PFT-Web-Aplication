package com.converters;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import com.entities.Departamento;
import com.services.DepartamentoService;

@Named(value = "departamentoConverter")
@SessionScoped
@FacesConverter("DepartamentoConverter")
public class DepartamentoConverter implements Serializable, Converter<Object> {
	@EJB
	private DepartamentoService departamentoService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        Long codigoDepartamento = Long.valueOf(value);
        
        Departamento departamentoDTO = departamentoService.obtenerPorIdDepartamento(codigoDepartamento);

        return departamentoDTO;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || !(value instanceof Departamento)) {
            return null;
        }

        Departamento departamentoDTO = (Departamento) value;
        
        // You may need to change this depending on which property uniquely identifies
        // the DepartamentoDTO object (e.g., use departamentoDTO.getCodigoDepartamento().toString() if codigoDepartamento is the unique identifier)
        return String.valueOf(departamentoDTO.getIdDepartamento());
    }
}
