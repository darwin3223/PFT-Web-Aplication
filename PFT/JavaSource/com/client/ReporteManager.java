package com.client;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import com.entities.Itr;
import com.entities.Reclamo;
import com.enums.TipoReclamo;
import com.services.ItrService;
import com.services.ReclamoService;
import com.utils.MessagesUtil;
import com.utils.PdfUtil;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionReporte")
public class ReporteManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject ReclamoService reclamoService;
	
	@Inject ItrService itrService;
	
	private long idItrSeleccionado;
	
	private Itr itrSeleccionado;
	
	private int mesSeleccionado;
	
	private int generacionSeleccionada;
	
	private TipoReclamo tipoReclamoSeleccionado;
	
	private String nombreArchivo;
	private String mensajeArchivo;

	@PostConstruct
	public void init() {
		
	}

	public StreamedContent reporteReclamo() {
		try {
			Date fechaActual = new Date(); 
			SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			String fechaFormateada = formatoDia.format(fechaActual);
			nombreArchivo = "Reporte de reclamos "+fechaFormateada;
			
			mensajeArchivo = "Reporte generado con los siguientes filtros: ";
			
			if (idItrSeleccionado != -1) {
				itrSeleccionado = itrService.get(idItrSeleccionado);
				mensajeArchivo = mensajeArchivo+"\nItr: "+itrSeleccionado.getNombre();
			}
			
			if (mesSeleccionado != 0) {
				mensajeArchivo = mensajeArchivo+"\nMes: "+mesSeleccionado;
			}
			
			if (generacionSeleccionada != -1) {
				mensajeArchivo = mensajeArchivo+"\nGeneración: "+generacionSeleccionada;
			}
			
			if (tipoReclamoSeleccionado != null) {
				mensajeArchivo = mensajeArchivo+"\nTipo de reclamo: "+tipoReclamoSeleccionado.getNombre();
			}
			
			List<Reclamo> listaReclamos = reclamoService.getByFiltros(itrSeleccionado, null, generacionSeleccionada, mesSeleccionado, tipoReclamoSeleccionado);
			
			mensajeArchivo = mensajeArchivo+"\nCantidad de reclamos obtenida: "+listaReclamos.size();

			MessagesUtil.createInfoNotification("Reporte generado", "El reporte de reclamos fue creado correctamente.");
			
			return PdfUtil.generarPDF(nombreArchivo, mensajeArchivo);
			
		} catch (Exception e) {
			MessagesUtil.createInfoNotification("Error", e.getMessage());
			return null;
		}
	}
	
	public void prueba() {
		Itr itr = itrService.get(2l);
		List<Reclamo> lista = reclamoService.getByFiltros(null, null, -1, 0, TipoReclamo.ACTIVIDAD_APE);
		
		System.out.println(lista);
	}
	
	public List<Itr> cargarListaItr() {
		return itrService.getAll();
	}
}
