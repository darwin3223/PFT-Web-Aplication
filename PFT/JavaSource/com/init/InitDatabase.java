package com.init;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.dto.AnalistaDTO;
import com.dto.EstadoSolicitudDTO;
import com.dto.EstudianteDTO;
import com.dto.ItrDTO;
import com.dto.JustificacionDTO;
import com.dto.ReclamoDTO;
import com.dto.TutorDTO;
import com.entities.Analista;
import com.entities.Departamento;
import com.entities.EstadoSolicitud;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Itr;
import com.entities.Justificacion;
import com.entities.Localidad;
import com.entities.Semestre;
import com.enums.EstadoEliminable;
import com.enums.EstadoEstudiante;
import com.enums.EstadoEvento;
import com.enums.EstadoItr;
import com.enums.EstadoUsuario;
import com.enums.Modalidad;
import com.enums.RolTutor;
import com.enums.TipoEvento;
import com.enums.TipoReclamo;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.exceptions.FieldValidationException;
import com.exceptions.ServiciosException;
import com.services.AnalistaService;
import com.services.DepartamentoService;
import com.services.EstadoSolicitudService;
import com.services.EstudianteService;
import com.services.EventoService;
import com.services.ItrService;
import com.services.JustificacionService;
import com.services.LocalidadService;
import com.services.ReclamoService;
import com.services.SemestreService;
import com.services.TutorService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@ViewScoped
@Named(value = "initDatabase")
public class InitDatabase implements Serializable {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject DepartamentoService depService;
	@Inject LocalidadService locService;
	@Inject ItrService itrService;
	@Inject AnalistaService anaService;
	@Inject EstudianteService estService;
	@Inject TutorService tutService;
	@Inject SemestreService semService;
	@Inject EstadoSolicitudService estadoService;
	@Inject EventoService eveService;
	@Inject JustificacionService jusService;
	@Inject ReclamoService recService;
	
	@PostConstruct
	public void comprobarDatabase() throws ServiciosException, FieldValidationException {
		if (depService.obtenerTodosDepartamento().size() < 1) {
			iniciarlizarDatabase();
		}
	}
	
	public void iniciarlizarDatabase() throws ServiciosException, FieldValidationException {
		insertarDepartamentos();
		insertarLocalidades();
		insertarItrs();
		insertarUsuarios();
		insertarSemestres();
		insertarEventos();
		insertarEstados();
		insertarJustificaciones();
		insertarReclamos();
	}
	
	public void insertarDepartamentos() throws ServiciosException {
		System.out.println("Se cargaron los departamentos");
		List<Departamento> listaDepartamentos = new LinkedList<>();
		listaDepartamentos.add(new Departamento(-1l,"Artigas"));
		listaDepartamentos.add(new Departamento(-1l,"Canelones"));
		listaDepartamentos.add(new Departamento(-1l,"Cerro Largo"));
		listaDepartamentos.add(new Departamento(-1l,"Colonia"));
		listaDepartamentos.add(new Departamento(-1l,"Durazno"));
		listaDepartamentos.add(new Departamento(-1l,"Flores"));
		listaDepartamentos.add(new Departamento(-1l,"Florida"));
		listaDepartamentos.add(new Departamento(-1l,"Lavalleja"));
		listaDepartamentos.add(new Departamento(-1l,"Maldonado"));
		listaDepartamentos.add(new Departamento(-1l,"Montevideo"));
		listaDepartamentos.add(new Departamento(-1l,"Paysandú"));
		listaDepartamentos.add(new Departamento(-1l,"Río Negro"));
		listaDepartamentos.add(new Departamento(-1l,"Rivera"));
		listaDepartamentos.add(new Departamento(-1l,"Rocha"));
		listaDepartamentos.add(new Departamento(-1l,"Salto"));
		listaDepartamentos.add(new Departamento(-1l,"San José"));
		listaDepartamentos.add(new Departamento(-1l,"Soriano"));
		listaDepartamentos.add(new Departamento(-1l,"Tacuarembó"));
		listaDepartamentos.add(new Departamento(-1l,"Treinta y Tres"));
		
		for (Departamento departamento : listaDepartamentos) {
			depService.save(departamento);
		}
	}
	
	public void insertarLocalidades() throws ServiciosException {
		List<Departamento> listaDeptos = depService.obtenerTodosDepartamento();
		List<Localidad> listaLocalidades = new LinkedList<>();
		listaLocalidades.add(new Localidad(-1l,"Artigas", listaDeptos.get(0)));
		listaLocalidades.add(new Localidad(-1l,"Canelones", listaDeptos.get(1)));
		listaLocalidades.add(new Localidad(-1l,"Melo", listaDeptos.get(2)));
		listaLocalidades.add(new Localidad(-1l,"Colonia del Sacramento", listaDeptos.get(3)));
		listaLocalidades.add(new Localidad(-1l,"Durazno", listaDeptos.get(4)));
		listaLocalidades.add(new Localidad(-1l,"Trinidad", listaDeptos.get(5)));
		listaLocalidades.add(new Localidad(-1l,"Florida", listaDeptos.get(6)));
		listaLocalidades.add(new Localidad(-1l,"Minas", listaDeptos.get(7)));
		listaLocalidades.add(new Localidad(-1l,"Maldonado", listaDeptos.get(8)));
		listaLocalidades.add(new Localidad(-1l,"Montevideo", listaDeptos.get(9)));
		listaLocalidades.add(new Localidad(-1l,"Paysandú", listaDeptos.get(10)));
		listaLocalidades.add(new Localidad(-1l,"Fray Bentos", listaDeptos.get(11)));
		listaLocalidades.add(new Localidad(-1l,"Rivera", listaDeptos.get(12)));
		listaLocalidades.add(new Localidad(-1l,"Rocha", listaDeptos.get(13)));
		listaLocalidades.add(new Localidad(-1l,"Salto", listaDeptos.get(14)));
		listaLocalidades.add(new Localidad(-1l,"San José de Mayo", listaDeptos.get(15)));
		listaLocalidades.add(new Localidad(-1l,"Mercedes", listaDeptos.get(16)));
		listaLocalidades.add(new Localidad(-1l,"Tacuarembó", listaDeptos.get(17)));
		listaLocalidades.add(new Localidad(-1l,"Treinta y Tres", listaDeptos.get(18)));
		
		for (Localidad localidad : listaLocalidades) {
			locService.save(localidad);
		}
	}
	
	public void insertarItrs() throws ServiciosException, FieldValidationException {
		System.out.println("Se cargaron los itrs");
		List<Itr> listaItrs = new LinkedList<>();
		List<Localidad> listaLocalidades = locService.obtenerTodosLocalidad();
		listaItrs.add(new Itr(-1l,"Itr SO",listaLocalidades.get(11),EstadoItr.ACTIVADO));
		listaItrs.add(new Itr(-1l,"Itr NORTE",listaLocalidades.get(12),EstadoItr.ACTIVADO));
		listaItrs.add(new Itr(-1l,"Itr CENTRO SUR",listaLocalidades.get(4),EstadoItr.ACTIVADO));
		
		for (Itr itr : listaItrs) {
			ItrDTO dto = itrService.itrToDTO(itr);
			itrService.save(dto);
		}
	}
	
	public void insertarUsuarios() throws FieldValidationException, ServiciosException {
		System.out.println("Se cargaron los usuarios");
		
		List<Itr> listaItrs = itrService.getAll();
		List<Localidad> listaLocalidades = locService.obtenerTodosLocalidad();
		AnalistaDTO usuario1 = new AnalistaDTO();
		usuario1.setNombre("Damián");
		usuario1.setApellido("Viera");
		usuario1.setDocumento("52899324");
		usuario1.setFechaNacimiento(crearFecha("13/09/2002"));
		usuario1.setNombreUsuario("damian.viera");
		usuario1.setContrasenia("Damian199333.");
		usuario1.setTipoUsuario(TipoUsuario.ANALISTA);
		usuario1.setVerificacion(Verificacion.VERIFICADO);
		usuario1.setMail("vieradamian06@gmail.com");
		usuario1.setMailInstitucional("damian.viera@estudiantes.utec.edu.uy");
		usuario1.setTelefono("099468310");
		usuario1.setGenero("MASCULINO");
		usuario1.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuario1.setItr(listaItrs.get(0));
		usuario1.setLocalidad(listaLocalidades.get(11));
		
		anaService.saveAnalista(usuario1);
		
		AnalistaDTO usuario2 = new AnalistaDTO();
		usuario2.setNombre("Franco");
		usuario2.setApellido("Borgiani");
		usuario2.setDocumento("20124022");
		usuario2.setFechaNacimiento(crearFecha("22/07/2004"));
		usuario2.setNombreUsuario("franco.borgiani");
		usuario2.setContrasenia("Franco1234@");
		usuario2.setTipoUsuario(TipoUsuario.ANALISTA);
		usuario2.setVerificacion(Verificacion.VERIFICADO);
		usuario2.setMail("franco.borgiani@gmail.com");
		usuario2.setMailInstitucional("franco.borgiani@estudiantes.utec.edu.uy");
		usuario2.setTelefono("091308679");
		usuario2.setGenero("MASCULINO");
		usuario2.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuario2.setItr(listaItrs.get(0));
		usuario2.setLocalidad(listaLocalidades.get(11));
		
		anaService.saveAnalista(usuario2);
		
		EstudianteDTO estudiante1 = new EstudianteDTO();
		AnalistaDTO usuario3 = new AnalistaDTO();
		usuario3.setNombre("Abril");
		usuario3.setApellido("Alanis");
		usuario3.setDocumento("17308542");
		usuario3.setFechaNacimiento(crearFecha("15/06/2003"));
		usuario3.setNombreUsuario("abril.alanis");
		usuario3.setContrasenia("Abril1234@");
		usuario3.setTipoUsuario(TipoUsuario.ESTUDIANTE);
		usuario3.setVerificacion(Verificacion.VERIFICADO);
		usuario3.setMail("abril.alanis@gmail.com");
		usuario3.setMailInstitucional("abril.alanis@estudiantes.utec.edu.uy");
		usuario3.setTelefono("092395923");
		usuario3.setGenero("FEMENINO");
		usuario3.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuario3.setItr(listaItrs.get(0));
		usuario3.setLocalidad(listaLocalidades.get(11));
		
		estudiante1.setAñoIngreso(2002);
		estudiante1.setEstadoEstudiante(EstadoEstudiante.MATRICULADO);
		
		estService.saveEstudiante(usuario3, estudiante1);
		
		TutorDTO tutor1 = new TutorDTO();
		AnalistaDTO usuario4 = new AnalistaDTO();
		usuario4.setNombre("Darwin");
		usuario4.setApellido("Alves");
		usuario4.setDocumento("58586147");
		usuario4.setFechaNacimiento(crearFecha("03/01/2001"));
		usuario4.setNombreUsuario("darwin.alves");
		usuario4.setContrasenia("Darwin1234@");
		usuario4.setTipoUsuario(TipoUsuario.TUTOR);
		usuario4.setVerificacion(Verificacion.VERIFICADO);
		usuario4.setMail("darwin.alves@gmail.com");
		usuario4.setMailInstitucional("darwin.alves@estudiantes.utec.edu.uy");
		usuario4.setTelefono("099384532");
		usuario4.setGenero("MASCULINO");
		usuario4.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuario4.setItr(listaItrs.get(0));
		usuario4.setLocalidad(listaLocalidades.get(11));
		
		tutor1.setArea("Infraestructura");
		tutor1.setRolTutor(RolTutor.ENCARGADO);
		
		tutService.saveTutor(usuario4, tutor1);
		
	}
	
	public void insertarSemestres() throws ServiciosException {
		System.out.println("Se cargaron los semestres");
		List<Semestre> listaSemestres = new LinkedList<>();
		listaSemestres.add(new Semestre(-1l,"Semestre 1"));
		listaSemestres.add(new Semestre(-1l,"Semestre 2"));
		listaSemestres.add(new Semestre(-1l,"Semestre 3"));
		listaSemestres.add(new Semestre(-1l,"Semestre 4"));
		listaSemestres.add(new Semestre(-1l,"Semestre 5"));
		listaSemestres.add(new Semestre(-1l,"Semestre 6"));
		listaSemestres.add(new Semestre(-1l,"Semestre 7"));
		listaSemestres.add(new Semestre(-1l,"Semestre 8"));
		
		for (Semestre semestre : listaSemestres) {
			semService.save(semestre);
		}
	}
	
	public void insertarEventos() throws ServiciosException {
		
		List<Itr> listaItrs = itrService.getAll();
		List<Semestre> listaSemestres = semService.obtenerTodos();
		List<Evento> listaEventos = new LinkedList<>();
		
		List<Semestre> semestresEvento1 = new LinkedList<>();
		semestresEvento1.add(listaSemestres.get(0));
		semestresEvento1.add(listaSemestres.get(1));
		semestresEvento1.add(listaSemestres.get(2));
		semestresEvento1.add(listaSemestres.get(3));
		
		Evento eve1 = new Evento(-1l,"CUTI BUSSINES FORUM 2024","Este es el evento de cuti",TipoEvento.EVENTO_VME,crearFecha("25/11/2024"),crearFecha("25/11/2024"),Modalidad.PRESENCIAL,"En montevideo",2,EstadoEvento.FUTURO,listaItrs.get(2),null);

		Query query = entityManager.createNativeQuery("SELECT LAST_NUMBER FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = 'SEQ_ID_EVENTO'");
		List<BigDecimal> lista = query.getResultList();
		System.out.println(lista);
		for (BigDecimal bigDecimal : lista) {
			eve1.setIdEvento(bigDecimal.longValueExact());
		}
		
		eve1.setListaSemestres(semestresEvento1);
		listaEventos.add(eve1);
		
		List<Semestre> semestresEvento2 = new LinkedList<>();
		semestresEvento2.add(listaSemestres.get(4));
		semestresEvento2.add(listaSemestres.get(5));
		semestresEvento2.add(listaSemestres.get(6));
		
		Evento eve2 = new Evento(eve1.getIdEvento()+1l,"Examen grupo GEDDA","Examen final del grupo GEDDA",TipoEvento.EXAMEN,crearFecha("18/12/2024"),crearFecha("25/11/2024"),Modalidad.PRESENCIAL,"ITR SO",4,EstadoEvento.FINALIZADO,listaItrs.get(0),null); 
		eve2.setListaSemestres(semestresEvento2);
		listaEventos.add(eve2);
		
		
		System.out.println(semestresEvento1);
		System.out.println(semestresEvento2);
		for (Evento e : listaEventos) {
			eveService.save(e);
		}
		System.out.println("Se cargaron los eventos");
	}
	
	public void insertarEstados() throws FieldValidationException, ServiciosException {
		System.out.println("Se cargaron los estados");
		List<EstadoSolicitudDTO> listaEstados = new LinkedList<>();
		
		listaEstados.add(new EstadoSolicitudDTO(-1l,"Ingresado",true,EstadoEliminable.NO_ELIMINABLE));
		listaEstados.add(new EstadoSolicitudDTO(-1l,"Finalizado",true,EstadoEliminable.NO_ELIMINABLE));
		listaEstados.add(new EstadoSolicitudDTO(-1l,"En progreso",true,EstadoEliminable.NO_ELIMINABLE));
		
		for (EstadoSolicitudDTO estado : listaEstados) {
			estadoService.save(estado);
		}
	}
	
	public void insertarJustificaciones() throws FieldValidationException, ServiciosException {
		
		
		List<Evento> listaEventos = eveService.getAll();
		Estudiante estu = new Estudiante();
		
		for (Estudiante e : estService.getEstudiantes()) {
			estu = e;
		}
		
		EstadoSolicitud estado = new EstadoSolicitud();
		
		for (EstadoSolicitud es : estadoService.getAll()) {
			estado = es;
		}
		
		System.out.println(estado);
		System.out.println(estadoService.getAll());
		
		List<JustificacionDTO> listaJustificaciones = new LinkedList<>();
		JustificacionDTO jus1 = new JustificacionDTO(-1l,"Esta es la justificación 1",crearFecha("22/09/2023"),null,estu,listaEventos.get(0),null);
		jus1.setEstado(estado);
		listaJustificaciones.add(jus1);
		listaJustificaciones.add(new JustificacionDTO(-1l,"Esta es la justificación 2",crearFecha("01/01/2025"),estado,estu,listaEventos.get(1),null));
		
		for (JustificacionDTO justificacion : listaJustificaciones) {
			jusService.save(justificacion,-1l);
		}
		System.out.println("Se cargaron las justificaciones");
	}
	
	public void insertarReclamos() throws FieldValidationException, ServiciosException {
		
		List<Evento> listaEventos = eveService.getAll();
		List<Semestre> listaSemestres = semService.obtenerTodos();
		
		Estudiante estu = new Estudiante();
		
		for (Estudiante e : estService.getEstudiantes()) {
			estu = e;
		}
		
		EstadoSolicitud estado = new EstadoSolicitud();
		
		estado = estadoService.getAll().get(0);
		
		List<ReclamoDTO> listaReclamos = new LinkedList<>();
		listaReclamos.add(new ReclamoDTO(-1l,"Creditos CUTI 2024",TipoReclamo.EVENTO_VME,"No recibí los creditos del evento",new Date(),listaSemestres.get(0),estado,estu,listaEventos.get(0),null));
		listaReclamos.add(new ReclamoDTO(-1l,"Reclamo 2",TipoReclamo.ACTIVIDAD_APE,"Este es el reclamo 2",new Date(),listaSemestres.get(1),estado,estu,listaEventos.get(0),null));
	
		for (ReclamoDTO reclamoDTO : listaReclamos) {
			recService.save(reclamoDTO);
		}
		System.out.println("Se cargaron los reclamos");
	}
	
	public Date crearFecha(String fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaResultado = null;
		try {
			fechaResultado = sdf.parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fechaResultado;
	}
	
	public void alterSecuence(long resta) {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe"; // Cambia esto a tu URL de conexión
        String username = "PFTDB";
        String password = "PFTDB"; // El valor deseado para reiniciar la secuencia

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Statement statement = connection.createStatement();
            String sql = "ALTER SEQUENCE SEQ_ID_EVENTO RESTART WITH " + resta;
            statement.execute(sql);
            System.out.println("Secuencia reiniciada con el valor deseado: " + resta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
