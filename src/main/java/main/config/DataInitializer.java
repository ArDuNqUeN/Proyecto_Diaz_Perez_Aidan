package main.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import main.model.*;
import main.repository.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlumnoRepository alumnoRepository;
    private final AdministradorRepository administradorRepository;
    private final TutorEmpresaRepository tutorEmpresaRepository;
    private final TutorCentroRepository tutorCentroRepository;
    private final EmpresaRepository empresaRepository;
    private final PracticaRepository practicaRepository;
    private final RegistroHorasRepository registroHorasRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final Random random = new Random();

    public DataInitializer(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AlumnoRepository alumnoRepository,
                          AdministradorRepository administradorRepository,
                          TutorEmpresaRepository tutorEmpresaRepository,
                          TutorCentroRepository tutorCentroRepository,
                          EmpresaRepository empresaRepository,
                          PracticaRepository practicaRepository,
                          RegistroHorasRepository registroHorasRepository,
                          EvaluacionRepository evaluacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.alumnoRepository = alumnoRepository;
        this.administradorRepository = administradorRepository;
        this.tutorEmpresaRepository = tutorEmpresaRepository;
        this.tutorCentroRepository = tutorCentroRepository;
        this.empresaRepository = empresaRepository;
        this.practicaRepository = practicaRepository;
        this.registroHorasRepository = registroHorasRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Limpiar todas las tablas para empezar de cero
        if (usuarioRepository.count() > 0) {
            System.out.println("🧹 Limpiando datos anteriores...");
            evaluacionRepository.deleteAll();
            registroHorasRepository.deleteAll();
            practicaRepository.deleteAll();
            tutorEmpresaRepository.deleteAll();
            tutorCentroRepository.deleteAll();
            alumnoRepository.deleteAll();
            administradorRepository.deleteAll();
            empresaRepository.deleteAll();
            usuarioRepository.deleteAll();
            System.out.println("✅ Datos anteriores eliminados.");
        }

        // ==================== ADMINISTRADOR ====================
        Usuario adminUser = crearUsuario("Administrador Principal", "admin@test.com", "admin123", "ROLE_ADMIN");
        Administrador admin = new Administrador();
        admin.setUsuario(adminUser);
        admin.setDepartamento("Dirección");
        administradorRepository.save(admin);

        // ==================== EMPRESAS ====================
        List<Empresa> empresas = new ArrayList<>();
        empresas.add(crearEmpresa("Tech Solutions S.L.", "Calle Mayor 15, 28013 Madrid", "Tecnología", "B12345678"));
        empresas.add(crearEmpresa("DataCorp España", "Av. Diagonal 540, 08017 Barcelona", "Big Data & IA", "B87654321"));
        empresas.add(crearEmpresa("Green Energy Systems", "Calle Alcalá 200, 28028 Madrid", "Energías Renovables", "B11223344"));
        empresas.add(crearEmpresa("Arquitectura Digital S.A.", "Gran Vía 45, 28013 Madrid", "Arquitectura y BIM", "B55667788"));
        empresas.add(crearEmpresa("SaludTech Innovaciones", "Ronda de Poniente 12, 28760 Tres Cantos", "Salud Digital", "B99887766"));

        // ==================== TUTORES DE CENTRO ====================
        List<TutorCentro> tutoresCentro = new ArrayList<>();
        tutoresCentro.add(crearTutorCentro("Laura Fernández", "tutor.centro1@test.com", "tutor123", "Desarrollo de Aplicaciones Web", "A-204"));
        tutoresCentro.add(crearTutorCentro("Miguel Ángel Torres", "tutor.centro2@test.com", "tutor123", "Administración de Sistemas", "B-105"));
        tutoresCentro.add(crearTutorCentro("Sofía Hernández", "tutor.centro3@test.com", "tutor123", "Marketing Digital", "C-302"));

        // ==================== TUTORES DE EMPRESA ====================
        List<TutorEmpresa> tutoresEmpresa = new ArrayList<>();
        tutoresEmpresa.add(crearTutorEmpresa("Roberto Gómez", "tutor.empresa1@test.com", "tutor123", "Supervisor de Desarrollo", "611222333", empresas.get(0)));
        tutoresEmpresa.add(crearTutorEmpresa("Patricia Vidal", "tutor.empresa2@test.com", "tutor123", "Directora de Datos", "622333444", empresas.get(1)));
        tutoresEmpresa.add(crearTutorEmpresa("Alberto Ruiz", "tutor.empresa3@test.com", "tutor123", "Responsable de Sostenibilidad", "633444555", empresas.get(2)));
        tutoresEmpresa.add(crearTutorEmpresa("Carmen Sánchez", "tutor.empresa4@test.com", "tutor123", "Jefa de Proyectos BIM", "644555666", empresas.get(3)));
        tutoresEmpresa.add(crearTutorEmpresa("Javier López", "tutor.empresa5@test.com", "tutor123", "Director de I+D+i", "655666777", empresas.get(4)));

        // ==================== ALUMNOS ====================
        List<Alumno> alumnos = new ArrayList<>();
        alumnos.add(crearAlumno("Carlos García López", "alumno1@test.com", "alumno123", "A2024001", "García López", "612345678"));
        alumnos.add(crearAlumno("María Martínez Ruiz", "alumno2@test.com", "alumno123", "A2024002", "Martínez Ruiz", "698765432"));
        alumnos.add(crearAlumno("Juan Pérez Sánchez", "alumno3@test.com", "alumno123", "A2024003", "Pérez Sánchez", "655111222"));
        alumnos.add(crearAlumno("Ana Jiménez Torres", "alumno4@test.com", "alumno123", "A2024004", "Jiménez Torres", "644333111"));
        alumnos.add(crearAlumno("Luis Fernández Gómez", "alumno5@test.com", "alumno123", "A2024005", "Fernández Gómez", "633444555"));
        alumnos.add(crearAlumno("Sara López Martín", "alumno6@test.com", "alumno123", "A2024006", "López Martín", "622555777"));
        alumnos.add(crearAlumno("Pablo Ruiz Díaz", "alumno7@test.com", "alumno123", "A2024007", "Ruiz Díaz", "611666888"));
        alumnos.add(crearAlumno("Elena García Hernández", "alumno8@test.com", "alumno123", "A2024008", "García Hernández", "699777999"));
        alumnos.add(crearAlumno("Daniel Sánchez Pérez", "alumno9@test.com", "alumno123", "A2024009", "Sánchez Pérez", "688888000"));
        alumnos.add(crearAlumno("Marta Torres Gómez", "alumno10@test.com", "alumno123", "A2024010", "Torres Gómez", "677999111"));
        alumnos.add(crearAlumno("Javier Díaz López", "alumno11@test.com", "alumno123", "A2024011", "Díaz López", "666000222"));
        alumnos.add(crearAlumno("Lucía Martín Ruiz", "alumno12@test.com", "alumno123", "A2024012", "Martín Ruiz", "655111333"));

        // ==================== ASIGNACIÓN DE PRÁCTICAS ====================
        // Asignamos 2-3 alumnos por tutor de empresa (máx 3)
        // tutorEmpresa 0 -> alumnos 0,1,2
        // tutorEmpresa 1 -> alumnos 3,4
        // tutorEmpresa 2 -> alumnos 5,6,7
        // tutorEmpresa 3 -> alumnos 8,9
        // tutorEmpresa 4 -> alumnos 10,11

        List<Practica> practicas = new ArrayList<>();
        practicas.add(crearPractica("Desarrollo Web Full Stack", "Prácticas de desarrollo web con Spring Boot y Angular.", 
                LocalDate.of(2025, 10, 1), LocalDate.of(2026, 3, 31), 400, EstadoPractica.ACTIVA,
                alumnos.get(0), empresas.get(0), tutoresEmpresa.get(0), tutoresCentro.get(0)));
        practicas.add(crearPractica("Administración de Sistemas Cloud", "Gestión de servidores en AWS y Azure.", 
                LocalDate.of(2025, 11, 15), LocalDate.of(2026, 5, 15), 500, EstadoPractica.ACTIVA,
                alumnos.get(1), empresas.get(0), tutoresEmpresa.get(0), tutoresCentro.get(1)));
        practicas.add(crearPractica("Ciberseguridad", "Auditoría de seguridad y pentesting.", 
                LocalDate.of(2026, 1, 10), LocalDate.of(2026, 7, 10), 450, EstadoPractica.ACTIVA,
                alumnos.get(2), empresas.get(0), tutoresEmpresa.get(0), tutoresCentro.get(0)));

        practicas.add(crearPractica("Big Data Analytics", "Análisis de datos con Spark y Python.", 
                LocalDate.of(2025, 9, 1), LocalDate.of(2026, 2, 28), 380, EstadoPractica.FINALIZADA,
                alumnos.get(3), empresas.get(1), tutoresEmpresa.get(1), tutoresCentro.get(1)));
        practicas.add(crearPractica("Machine Learning", "Desarrollo de modelos predictivos.", 
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 9, 1), 480, EstadoPractica.ACTIVA,
                alumnos.get(4), empresas.get(1), tutoresEmpresa.get(1), tutoresCentro.get(2)));

        practicas.add(crearPractica("Energía Solar Fotovoltaica", "Diseño de plantas solares.", 
                LocalDate.of(2026, 1, 15), LocalDate.of(2026, 6, 15), 400, EstadoPractica.ACTIVA,
                alumnos.get(5), empresas.get(2), tutoresEmpresa.get(2), tutoresCentro.get(0)));
        practicas.add(crearPractica("Eficiencia Energética", "Auditorías energéticas en edificios.", 
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 8, 1), 420, EstadoPractica.ACTIVA,
                alumnos.get(6), empresas.get(2), tutoresEmpresa.get(2), tutoresCentro.get(1)));
        practicas.add(crearPractica("Movilidad Sostenible", "Desarrollo de infraestructura de recarga.", 
                LocalDate.of(2025, 11, 1), LocalDate.of(2026, 4, 30), 350, EstadoPractica.FINALIZADA,
                alumnos.get(7), empresas.get(2), tutoresEmpresa.get(2), tutoresCentro.get(2)));

        practicas.add(crearPractica("Diseño BIM", "Modelado de edificios con Revit.", 
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 8, 31), 450, EstadoPractica.ACTIVA,
                alumnos.get(8), empresas.get(3), tutoresEmpresa.get(3), tutoresCentro.get(0)));
        practicas.add(crearPractica("Realidad Virtual en Arquitectura", "Desarrollo de entornos inmersivos.", 
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 10, 1), 400, EstadoPractica.ACTIVA,
                alumnos.get(9), empresas.get(3), tutoresEmpresa.get(3), tutoresCentro.get(1)));

        practicas.add(crearPractica("Telemedicina", "Desarrollo de plataforma de consultas online.", 
                LocalDate.of(2026, 2, 15), LocalDate.of(2026, 8, 15), 500, EstadoPractica.ACTIVA,
                alumnos.get(10), empresas.get(4), tutoresEmpresa.get(4), tutoresCentro.get(2)));
        practicas.add(crearPractica("Wearables Sanitarios", "Integración de dispositivos IoT para salud.", 
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 11, 1), 480, EstadoPractica.ACTIVA,
                alumnos.get(11), empresas.get(4), tutoresEmpresa.get(4), tutoresCentro.get(0)));

        // Práctica cancelada de ejemplo
        practicas.add(crearPractica("Proyecto Cancelado", "Práctica que fue cancelada.", 
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), 300, EstadoPractica.CANCELADA,
                alumnos.get(0), empresas.get(1), tutoresEmpresa.get(1), tutoresCentro.get(0)));

        // Actualizar listas de prácticas en tutores de centro
        for (Practica p : practicas) {
            if (p.getTutorCentro() != null) {
                p.getTutorCentro().addPractica(p);
            }
        }
        for (TutorCentro tc : tutoresCentro) {
            tutorCentroRepository.save(tc);
        }

        // ==================== REGISTROS DE HORAS ====================
        for (Practica p : practicas) {
            if (p.getEstado() == EstadoPractica.CANCELADA) continue;
            generarRegistrosHoras(p, p.getAlumno());
        }

        // ==================== EVALUACIONES ====================
        for (Practica p : practicas) {
            if (p.getEstado() == EstadoPractica.CANCELADA) continue;
            if (p.getEstado() == EstadoPractica.FINALIZADA || random.nextBoolean()) {
                crearEvaluacion(p.getAlumno(), p, p.getTutorEmpresa(), null, 
                        5.0 + random.nextDouble() * 5.0, 
                        "Evaluación de desempeño durante las prácticas.");
            }
            if (p.getEstado() == EstadoPractica.FINALIZADA || random.nextBoolean()) {
                crearEvaluacion(p.getAlumno(), p, null, p.getTutorCentro(), 
                        5.0 + random.nextDouble() * 5.0, 
                        "Seguimiento académico de las prácticas.");
            }
        }

        System.out.println("🎉 Datos iniciales cargados correctamente.");
        System.out.println("📊 Resumen: " + usuarioRepository.count() + " usuarios, " 
                + empresas.size() + " empresas, " + tutoresEmpresa.size() + " tutores empresa, "
                + tutoresCentro.size() + " tutores centro, " + alumnos.size() + " alumnos, "
                + practicas.size() + " prácticas.");
    }

    private Usuario crearUsuario(String nombre, String email, String password, String rol) {
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRol(rol);
        u.setActivo(true);
        return usuarioRepository.save(u);
    }

    private Empresa crearEmpresa(String nombre, String direccion, String sector, String cif) {
        Empresa e = new Empresa();
        e.setNombre(nombre);
        e.setDireccion(direccion);
        e.setSector(sector);
        e.setCif(cif);
        return empresaRepository.save(e);
    }

    private TutorCentro crearTutorCentro(String nombre, String email, String password, String departamento, String despacho) {
        Usuario u = crearUsuario(nombre, email, password, "ROLE_TUTOR_CENTRO");
        TutorCentro tc = new TutorCentro();
        tc.setUsuario(u);
        tc.setDepartamento(departamento);
        tc.setDespacho(despacho);
        return tutorCentroRepository.save(tc);
    }

    private TutorEmpresa crearTutorEmpresa(String nombre, String email, String password, String cargo, String telefono, Empresa empresa) {
        Usuario u = crearUsuario(nombre, email, password, "ROLE_TUTOR_EMPRESA");
        TutorEmpresa te = new TutorEmpresa();
        te.setUsuario(u);
        te.setCargo(cargo);
        te.setTelefono(telefono);
        te.setEmpresa(empresa);
        return tutorEmpresaRepository.save(te);
    }

    private Alumno crearAlumno(String nombre, String email, String password, String matricula, String apellidos, String telefono) {
        Usuario u = crearUsuario(nombre, email, password, "ROLE_ALUMNO");
        Alumno a = new Alumno();
        a.setUsuario(u);
        a.setMatricula(matricula);
        a.setApellidos(apellidos);
        a.setTelefono(telefono);
        return alumnoRepository.save(a);
    }

    private Practica crearPractica(String titulo, String descripcion, LocalDate inicio, LocalDate fin, 
                                   int horas, EstadoPractica estado, Alumno alumno, Empresa empresa,
                                   TutorEmpresa tutorEmpresa, TutorCentro tutorCentro) {
        Practica p = new Practica();
        p.setTitulo(titulo);
        p.setDescripcion(descripcion);
        p.setFechaInicio(inicio);
        p.setFechaFin(fin);
        p.setHorasPrevistas(horas);
        p.setEstado(estado);
        p.setAlumno(alumno);
        p.setEmpresa(empresa);
        p.setTutorEmpresa(tutorEmpresa);
        p.setTutorCentro(tutorCentro);
        return practicaRepository.save(p);
    }

    private void generarRegistrosHoras(Practica practica, Alumno alumno) {
        LocalDate start = practica.getFechaInicio();
        LocalDate end = practica.getFechaFin();
        if (practica.getEstado() == EstadoPractica.FINALIZADA) {
            end = practica.getFechaFin();
        } else {
            end = LocalDate.now().isBefore(end) ? LocalDate.now().minusDays(1) : end;
        }
        if (start.isAfter(end)) return;

        List<LocalDate> fechas = new ArrayList<>();
        LocalDate actual = start;
        while (!actual.isAfter(end)) {
            if (actual.getDayOfWeek().getValue() <= 5) { // Lunes a Viernes
                fechas.add(actual);
            }
            actual = actual.plusDays(1);
        }

        int totalDias = Math.min(20, fechas.size()); // máximo 20 registros
        for (int i = 0; i < totalDias; i++) {
            LocalDate fecha = fechas.get(i);
            int horas = 4 + random.nextInt(5); // 4-8 horas
            EstadoRegistro estado;
            if (practica.getEstado() == EstadoPractica.FINALIZADA) {
                estado = random.nextDouble() < 0.9 ? EstadoRegistro.VALIDADA : EstadoRegistro.RECHAZADA;
            } else {
                double r = random.nextDouble();
                estado = r < 0.6 ? EstadoRegistro.VALIDADA : (r < 0.9 ? EstadoRegistro.PENDIENTE : EstadoRegistro.RECHAZADA);
            }
            RegistroHoras rh = new RegistroHoras();
            rh.setFecha(fecha);
            rh.setHoras(horas);
            rh.setDescripcion("Tareas realizadas el " + fecha);
            rh.setEstado(estado);
            rh.setAlumno(alumno);
            rh.setPractica(practica);
            registroHorasRepository.save(rh);
        }
    }

    private void crearEvaluacion(Alumno alumno, Practica practica, TutorEmpresa tutorEmpresa, TutorCentro tutorCentro, 
                                 double nota, String comentario) {
        Evaluacion ev = new Evaluacion();
        ev.setNota(Math.round(nota * 10.0) / 10.0);
        ev.setComentario(comentario);
        ev.setTipoEvaluador(tutorEmpresa != null ? TipoEvaluador.TUTOR_EMPRESA : TipoEvaluador.TUTOR_CENTRO);
        ev.setAlumno(alumno);
        ev.setPractica(practica);
        ev.setTutorEmpresa(tutorEmpresa);
        ev.setTutorCentro(tutorCentro);
        evaluacionRepository.save(ev);
    }
}