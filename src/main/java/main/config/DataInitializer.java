package main.config;

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

    public DataInitializer(UsuarioRepository usuarioRepository, 
                          PasswordEncoder passwordEncoder,
                          AlumnoRepository alumnoRepository,
                          AdministradorRepository administradorRepository,
                          TutorEmpresaRepository tutorEmpresaRepository,
                          TutorCentroRepository tutorCentroRepository,
                          EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.alumnoRepository = alumnoRepository;
        this.administradorRepository = administradorRepository;
        this.tutorEmpresaRepository = tutorEmpresaRepository;
        this.tutorCentroRepository = tutorCentroRepository;
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // ========== 1. CREAR USUARIO ADMINISTRADOR ==========
        if (usuarioRepository.findByEmail("admin@test.com").isEmpty()) {
            // Crear el Usuario base
            Usuario usuarioAdmin = new Usuario();
            usuarioAdmin.setNombre("Administrador Principal");
            usuarioAdmin.setEmail("admin@test.com");
            usuarioAdmin.setPassword(passwordEncoder.encode("admin123"));
            usuarioAdmin.setRol("ROLE_ADMIN");
            usuarioAdmin.setActivo(true);
            usuarioAdmin = usuarioRepository.save(usuarioAdmin);

            // Crear el perfil de Administrador vinculado
            Administrador admin = new Administrador();
            admin.setUsuario(usuarioAdmin);
            admin.setDepartamento("Informática");
            administradorRepository.save(admin);

            System.out.println("✅ Administrador creado: admin@test.com / admin123");
        }

        // ========== 2. CREAR USUARIO ALUMNO ==========
        if (usuarioRepository.findByEmail("alumno@test.com").isEmpty()) {
            Usuario usuarioAlumno = new Usuario();
            usuarioAlumno.setNombre("Carlos");
            usuarioAlumno.setEmail("alumno@test.com");
            usuarioAlumno.setPassword(passwordEncoder.encode("alumno123"));
            usuarioAlumno.setRol("ROLE_ALUMNO");
            usuarioAlumno.setActivo(true);
            usuarioAlumno = usuarioRepository.save(usuarioAlumno);

            Alumno alumno = new Alumno();
            alumno.setUsuario(usuarioAlumno);
            alumno.setMatricula("A2024001");
            alumno.setApellidos("García López");
            alumno.setTelefono("612345678");
            alumnoRepository.save(alumno);

            System.out.println("✅ Alumno creado: alumno@test.com / alumno123");
        }

        // ========== 3. CREAR USUARIO ALUMNO 2 (para pruebas) ==========
        if (usuarioRepository.findByEmail("maria@test.com").isEmpty()) {
            Usuario usuarioAlumno2 = new Usuario();
            usuarioAlumno2.setNombre("María");
            usuarioAlumno2.setEmail("maria@test.com");
            usuarioAlumno2.setPassword(passwordEncoder.encode("alumno123"));
            usuarioAlumno2.setRol("ROLE_ALUMNO");
            usuarioAlumno2.setActivo(true);
            usuarioAlumno2 = usuarioRepository.save(usuarioAlumno2);

            Alumno alumno2 = new Alumno();
            alumno2.setUsuario(usuarioAlumno2);
            alumno2.setMatricula("A2024002");
            alumno2.setApellidos("Martínez Ruiz");
            alumno2.setTelefono("698765432");
            alumnoRepository.save(alumno2);

            System.out.println("✅ Alumna creada: maria@test.com / alumno123");
        }

        // ========== 4. CREAR EMPRESA DE PRUEBA ==========
        Empresa empresa = null;
        if (empresaRepository.findByCif("B12345678").isEmpty()) {
            empresa = new Empresa();
            empresa.setNombre("Tech Solutions S.L.");
            empresa.setDireccion("Calle Mayor 15, 28013 Madrid");
            empresa.setSector("Tecnología");
            empresa.setCif("B12345678");
            empresa = empresaRepository.save(empresa);
            System.out.println("✅ Empresa creada: Tech Solutions S.L.");
        } else {
            empresa = empresaRepository.findByCif("B12345678").get();
        }

        // ========== 5. CREAR TUTOR DE EMPRESA ==========
        if (usuarioRepository.findByEmail("tutor.empresa@test.com").isEmpty()) {
            Usuario usuarioTutorEmpresa = new Usuario();
            usuarioTutorEmpresa.setNombre("Roberto");
            usuarioTutorEmpresa.setEmail("tutor.empresa@test.com");
            usuarioTutorEmpresa.setPassword(passwordEncoder.encode("tutor123"));
            usuarioTutorEmpresa.setRol("ROLE_TUTOR_EMPRESA");
            usuarioTutorEmpresa.setActivo(true);
            usuarioTutorEmpresa = usuarioRepository.save(usuarioTutorEmpresa);

            TutorEmpresa tutorEmpresa = new TutorEmpresa();
            tutorEmpresa.setUsuario(usuarioTutorEmpresa);
            tutorEmpresa.setCargo("Supervisor de Desarrollo");
            tutorEmpresa.setTelefono("611222333");
            if (empresa != null) {
                tutorEmpresa.setEmpresa(empresa);
            }
            tutorEmpresaRepository.save(tutorEmpresa);

            System.out.println("✅ Tutor Empresa creado: tutor.empresa@test.com / tutor123");
        }

        // ========== 6. CREAR TUTOR DE CENTRO ==========
        if (usuarioRepository.findByEmail("tutor.centro@test.com").isEmpty()) {
            Usuario usuarioTutorCentro = new Usuario();
            usuarioTutorCentro.setNombre("Laura");
            usuarioTutorCentro.setEmail("tutor.centro@test.com");
            usuarioTutorCentro.setPassword(passwordEncoder.encode("tutor123"));
            usuarioTutorCentro.setRol("ROLE_TUTOR_CENTRO");
            usuarioTutorCentro.setActivo(true);
            usuarioTutorCentro = usuarioRepository.save(usuarioTutorCentro);

            TutorCentro tutorCentro = new TutorCentro();
            tutorCentro.setUsuario(usuarioTutorCentro);
            tutorCentro.setDepartamento("Desarrollo de Aplicaciones Web");
            tutorCentro.setDespacho("A-204");
            tutorCentroRepository.save(tutorCentro);

            System.out.println("✅ Tutor Centro creado: tutor.centro@test.com / tutor123");
        }

        System.out.println("🎉 Datos iniciales cargados correctamente.");
    }
}