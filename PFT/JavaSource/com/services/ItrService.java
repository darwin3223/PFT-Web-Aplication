package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dao.ItrDAO;
import com.dto.ItrDTO;
import com.entities.Itr;
import com.entities.Localidad;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.repositories.ItrRepository;
import com.repositories.Repository;
import com.validation.ValidationError;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class ItrService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = ItrRepository.class, beanName="ItrDAO")
	private ItrRepository dao;
	
	@Inject private LocalidadService localidadService;
	@Inject private ItrService itrService;
	
	public Itr save(ItrDTO dto) throws ServiciosException, FieldValidationException {
		ValidationError.validarItr(dto,itrService);
		Itr itrNuevo = dtoToItr(dto);
		long idLocalidad = itrNuevo.getLocalidad().getIdLocalidad();
		itrNuevo.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidad));
		dao.save(itrNuevo);
		return itrNuevo;
	}
	
	public Itr update(ItrDTO dto) throws ServiciosException, FieldValidationException {
		ValidationError.validarItr(dto, itrService);
		Itr itr = dtoToItr(dto);
		dao.update(itr);
		return itr;
	}
	
	public void deleteLogical(long id) throws ServiciosException {
		dao.delete(dao.get(id));
	}
	
	public List<Itr> getAll(){
		return dao.getAll();
	}
	
	public Itr get(long id) {
		return dao.get(id);
	}
	
	public Itr dtoToItr (ItrDTO DTO) {
		Itr itr = new Itr();
		itr.setIdItr(DTO.getIdItr());
		itr.setNombre(DTO.getNombre());
		itr.setLocalidad(DTO.getLocalidad());
		itr.setEstado(DTO.getEstado());
		return itr;
	}
	
	public ItrDTO itrToDTO(Itr itr) {
		return itr == null
				? null
				: ItrDTO.builder()
					.idItr(itr.getIdItr())
					.nombre(itr.getNombre())
					.localidad(itr.getLocalidad())
					.estado(itr.getEstado())
					.departamento(
						localidadExiste(itr.getLocalidad())
							? itr.getLocalidad().getDepartamento()
							: null)
					.build();
	}
	
	private boolean localidadExiste(Localidad l) {
		return l != null;
	}
	
	public Itr findByName(String name) throws ServiciosException {
		return dao.findByName(name);
	}
	
}
