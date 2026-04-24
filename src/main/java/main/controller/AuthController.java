package main.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import main.model.Usuario;
import main.service.AdministradorService;
import main.service.AlumnoService;
import main.service.TutorCentroService;
import main.service.TutorEmpresaService;
import main.service.UsuarioService;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final AlumnoService alumnoService;
    private final AdministradorService administradorService;
    private final TutorEmpresaService tutorEmpresaService;
    private final TutorCentroService tutorCentroService;

    public AuthController(UsuarioService usuarioService, AlumnoService alumnoService,
                         AdministradorService administradorService,
                         TutorEmpresaService tutorEmpresaService,
                         TutorCentroService tutorCentroService) {
        this.usuarioService = usuarioService;
        this.alumnoService = alumnoService;
        this.administradorService = administradorService;
        this.tutorEmpresaService = tutorEmpresaService;
        this.tutorCentroService = tutorCentroService;
    }

    // Página de inicio que ven todos ANTES de loguearse
    @GetMapping("/")
    public String inicio() {
        return "index"; // resources/templates/index.html
    }

    // Página de login personalizada
    @GetMapping("/login")
    public String login() {
        return "login"; // resources/templates/login.html
    }

    // Redirección después del login según el ROL
    @GetMapping("/panel")
    public String redirigirPorRol() {
        // Obtenemos el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String rol = usuario.getRol();

            // Redirigimos según el rol
            switch (rol) {
                case "ROLE_ADMIN":
                    return "redirect:/panel/admin";
                case "ROLE_ALUMNO":
                    return "redirect:/panel/alumno";
                case "ROLE_TUTOR_EMPRESA":
                    return "redirect:/panel/tutor-empresa";
                case "ROLE_TUTOR_CENTRO":
                    return "redirect:/panel/tutor-centro";
                default:
                    return "redirect:/login?error";
            }
        }
        return "redirect:/login?error";
    }
}