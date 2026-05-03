package main.config;

import java.time.LocalDate;

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

    public DataInitializer(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AlumnoRepository alumnoRepository,
                          AdministradorRepository administradorRepository,
                          TutorEmpresaRepository tutorEmpresaRepository,
                          TutorCentroRepository tutorCentroRepository,
                          EmpresaRepository empresaRepository,
                          PracticaRepository practicaRepository,
                          RegistroHorasRepository registroHorasRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.alumnoRepository = alumnoRepository;
        this.administradorRepository = administradorRepository;
        this.tutorEmpresaRepository = tutorEmpresaRepository;
        this.tutorCentroRepository = tutorCentroRepository;
        this.empresaRepository = empresaRepository;
        this.practicaRepository = practicaRepository;
        this.registroHorasRepository = registroHorasRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // ========== ADMINISTRADOR ==========
        if (usuarioRepository.findByEmail("admin@test.com").isEmpty()) {
            Usuario usuarioAdmin = new Usuario();
            usuarioAdmin.setNombre("Administrador Principal");
            usuarioAdmin.setEmail("admin@test.com");
            usuarioAdmin.setPassword(passwordEncoder.encode("admin123"));
            usuarioAdmin.setRol("ROLE_ADMIN");
            usuarioAdmin.setActivo(true);
            usuarioAdmin = usuarioRepository.save(usuarioAdmin);

            Administrador admin = new Administrador();
            admin.setUsuario(usuarioAdmin);
            admin.setDepartamento("Informatica");
            administradorRepository.save(admin);

            System.out.println("Admin creado: admin@test.com / admin123");
        }

        // ========== ALUMNO 1 ==========
        Alumno alumno1 = null;
        if (usuarioRepository.findByEmail("alumno@test.com").isEmpty()) {
            Usuario usuarioAlumno = new Usuario();
            usuarioAlumno.setNombre("Carlos");
            usuarioAlumno.setEmail("alumno@test.com");
            usuarioAlumno.setPassword(passwordEncoder.encode("alumno123"));
            usuarioAlumno.setRol("ROLE_ALUMNO");
            usuarioAlumno.setActivo(true);
            usuarioAlumno = usuarioRepository.save(usuarioAlumno);

            alumno1 = new Alumno();
            alumno1.setUsuario(usuarioAlumno);
            alumno1.setMatricula("A2024001");
            alumno1.setApellidos("Garcia Lopez");
            alumno1.setTelefono("612345678");
            alumno1 = alumnoRepository.save(alumno1);

            System.out.println("Alumno creado: alumno@test.com / alumno123");
        } else {
            alumno1 = alumnoRepository.findByUsuario(
                usuarioRepository.findByEmail("alumno@test.com").get()
            ).orElse(null);
        }

        // ========== ALUMNO 2 ==========
        if (usuarioRepository.findByEmail("maria@test.com").isEmpty()) {
            Usuario usuarioAlumno2 = new Usuario();
            usuarioAlumno2.setNombre("Maria");
            usuarioAlumno2.setEmail("maria@test.com");
            usuarioAlumno2.setPassword(passwordEncoder.encode("alumno123"));
            usuarioAlumno2.setRol("ROLE_ALUMNO");
            usuarioAlumno2.setActivo(true);
            usuarioAlumno2 = usuarioRepository.save(usuarioAlumno2);

            Alumno alumno2 = new Alumno();
            alumno2.setUsuario(usuarioAlumno2);
            alumno2.setMatricula("A2024002");
            alumno2.setApellidos("Martinez Ruiz");
            alumno2.setTelefono("698765432");
            alumnoRepository.save(alumno2);

            System.out.println("Alumna creada: maria@test.com / alumno123");
        }

        // ========== EMPRESA ==========
        Empresa empresa = null;
        if (empresaRepository.findByCif("B12345678").isEmpty()) {
            empresa = new Empresa();
            empresa.setNombre("Tech Solutions S.L.");
            empresa.setDireccion("Calle Mayor 15, 28013 Madrid");
            empresa.setSector("Tecnologia");
            empresa.setCif("B12345678");
            empresa = empresaRepository.save(empresa);
            System.out.println("Empresa creada: Tech Solutions S.L.");
        } else {
            empresa = empresaRepository.findByCif("B12345678").get();
        }

        // ========== TUTOR EMPRESA ==========
        TutorEmpresa tutorEmpresa = null;
        if (usuarioRepository.findByEmail("tutor.empresa@test.com").isEmpty()) {
            Usuario usuarioTutorEmpresa = new Usuario();
            usuarioTutorEmpresa.setNombre("Roberto");
            usuarioTutorEmpresa.setEmail("tutor.empresa@test.com");
            usuarioTutorEmpresa.setPassword(passwordEncoder.encode("tutor123"));
            usuarioTutorEmpresa.setRol("ROLE_TUTOR_EMPRESA");
            usuarioTutorEmpresa.setActivo(true);
            usuarioTutorEmpresa = usuarioRepository.save(usuarioTutorEmpresa);

            tutorEmpresa = new TutorEmpresa();
            tutorEmpresa.setUsuario(usuarioTutorEmpresa);
            tutorEmpresa.setCargo("Supervisor de Desarrollo");
            tutorEmpresa.setTelefono("611222333");
            tutorEmpresa.setEmpresa(empresa);
            tutorEmpresa = tutorEmpresaRepository.save(tutorEmpresa);

            System.out.println("Tutor Empresa creado: tutor.empresa@test.com / tutor123");
        } else {
            tutorEmpresa = tutorEmpresaRepository.findByUsuario(
                usuarioRepository.findByEmail("tutor.empresa@test.com").get()
            ).orElse(null);
        }

        // ========== TUTOR CENTRO ==========
        TutorCentro tutorCentro = null;
        if (usuarioRepository.findByEmail("tutor.centro@test.com").isEmpty()) {
            Usuario usuarioTutorCentro = new Usuario();
            usuarioTutorCentro.setNombre("Laura");
            usuarioTutorCentro.setEmail("tutor.centro@test.com");
            usuarioTutorCentro.setPassword(passwordEncoder.encode("tutor123"));
            usuarioTutorCentro.setRol("ROLE_TUTOR_CENTRO");
            usuarioTutorCentro.setActivo(true);
            usuarioTutorCentro = usuarioRepository.save(usuarioTutorCentro);

            tutorCentro = new TutorCentro();
            tutorCentro.setUsuario(usuarioTutorCentro);
            tutorCentro.setDepartamento("Desarrollo de Aplicaciones Web");
            tutorCentro.setDespacho("A-204");
            tutorCentro = tutorCentroRepository.save(tutorCentro);

            System.out.println("Tutor Centro creado: tutor.centro@test.com / tutor123");
        } else {
            tutorCentro = tutorCentroRepository.findByUsuario(
                usuarioRepository.findByEmail("tutor.centro@test.com").get()
            ).orElse(null);
        }

        // ========== PRACTICA ==========
        if (practicaRepository.count() == 0 && alumno1 != null && empresa != null
            && tutorEmpresa != null && tutorCentro != null) {

            Practica practica = new Practica();
            practica.setTitulo("Desarrollo Web Full Stack");
            practica.setDescripcion("Practicas de desarrollo web con Spring Boot y Angular.");
            practica.setFechaInicio(LocalDate.of(2026, 1, 15));
            practica.setFechaFin(LocalDate.of(2026, 6, 15));
            practica.setHorasPrevistas(400);
            practica.setEstado(EstadoPractica.ACTIVA);
            practica.setAlumno(alumno1);
            practica.setEmpresa(empresa);
            practica.setTutorEmpresa(tutorEmpresa);
            practica.setTutorCentro(tutorCentro);
            practica = practicaRepository.save(practica);

            // Forzar relación bidireccional
            tutorCentro.addPractica(practica);
            tutorCentroRepository.save(tutorCentro);

            System.out.println("Practica creada: " + practica.getTitulo());

            // Registros de horas
            RegistroHoras registro1 = new RegistroHoras();
            registro1.setFecha(LocalDate.of(2026, 4, 20));
            registro1.setHoras(8);
            registro1.setDescripcion("Configuracion del entorno de desarrollo.");
            registro1.setEstado(EstadoRegistro.VALIDADA);
            registro1.setAlumno(alumno1);
            registro1.setPractica(practica);
            registroHorasRepository.save(registro1);

            RegistroHoras registro2 = new RegistroHoras();
            registro2.setFecha(LocalDate.of(2026, 4, 21));
            registro2.setHoras(7);
            registro2.setDescripcion("Desarrollo de API REST con Spring Boot.");
            registro2.setEstado(EstadoRegistro.PENDIENTE);
            registro2.setAlumno(alumno1);
            registro2.setPractica(practica);
            registroHorasRepository.save(registro2);

            RegistroHoras registro3 = new RegistroHoras();
            registro3.setFecha(LocalDate.of(2026, 4, 22));
            registro3.setHoras(8);
            registro3.setDescripcion("Pruebas unitarias con JUnit y Mockito.");
            registro3.setEstado(EstadoRegistro.PENDIENTE);
            registro3.setAlumno(alumno1);
            registro3.setPractica(practica);
            registroHorasRepository.save(registro3);

            System.out.println("3 registros de horas creados.");
        }

        System.out.println("Datos iniciales cargados correctamente.");
    }
}