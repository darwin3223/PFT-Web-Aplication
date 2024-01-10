package com.client;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.util.LangUtils;

import com.dto.ConvocatoriaDTO;
import com.entities.Convocatoria;
import com.enums.TipoUsuario;
import com.services.ConvocatoriaService;
import com.services.EstudianteService;
import com.services.UsuarioService;
import com.utils.RenderVista;

import lombok.Data;

@Data
@ViewScoped
@Named(value = "gestionConvocatoria")
public class ConvocatoriaManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Convocatoria> listaConvocatoria;
	
	private RenderVista render;
	
	private TipoUsuario tipoUsuario;	// Tipo del usuario Logeado
	private long idUsuario;				// Id del usuario logeado
	
	private ConvocatoriaDTO convocatoria;
	
	@Inject private AutenticacionManager autenticacion;
	@Inject private JustificacionManager gestionListaJustificacion;
	@Inject private ReclamoManager gestionListarReclamo;
	
	@Inject private EstudianteService estudianteService;
	@Inject private UsuarioService usuarioService;
	@Inject private ConvocatoriaService convocatoriaService;
	
	@PostConstruct
	public void init() {
		idUsuario = autenticacion.loadIdUsuarioLogeado();
		tipoUsuario = autenticacion.loadTipoUsuarioLogeado();
		render = new RenderVista(tipoUsuario);
		refreshListaConvocatoria();
	}

	public boolean globalFilterFunctionJustificacion(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		Convocatoria convocatoria = (Convocatoria) value;
		return (convocatoria.getIdConvocatoria()+ "").toLowerCase().contains(filterText)
				|| convocatoria.getEvento().getTitulo().toLowerCase().contains(filterText)
				|| convocatoria.getTipoAsistencia().getNombre().toLowerCase().contains(filterText);
	}

	public void onClickReclamo(Convocatoria convocatoria) {
		this.convocatoria = convocatoriaService.convertConvocatoriaToDto(convocatoria);
		gestionListarReclamo.onClickReclamo(this.convocatoria);
	}
	
	public void onClickJustificacion(Convocatoria convocatoria) {
		this.convocatoria = convocatoriaService.convertConvocatoriaToDto(convocatoria);
		gestionListaJustificacion.createJustificacion(this.convocatoria);
	}
	
	public void onRefresh() {
		refreshListaConvocatoria();
	}

	private void refreshListaConvocatoria() {
		listaConvocatoria = convocatoriaService.getList(tipoUsuario, estudianteService.getEstudianteById(idUsuario));
	}
}
