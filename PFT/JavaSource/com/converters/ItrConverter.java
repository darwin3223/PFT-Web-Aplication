package com.converters;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import com.entities.Itr;
import com.services.ItrService;

import java.io.Serializable;

@Named(value = "itrConverter")
@SessionScoped
@FacesConverter("ItrConverter")
public class ItrConverter implements Serializable, Converter<Object> {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ItrService itrService;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        Long codigoItr = Long.valueOf(value);
        
        Itr itrDTO = itrService.get(codigoItr);

        return itrDTO;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || !(value instanceof Itr)) {
            return null;
        }

        Itr itrDTO = (Itr) value;
        
        // You may need to change this depending on which property uniquely identifies
        // the DepartamentoDTO object (e.g., use departamentoDTO.getCodigoDepartamento().toString() if codigoDepartamento is the unique identifier)
        return String.valueOf(itrDTO.getIdItr());
    }
}
