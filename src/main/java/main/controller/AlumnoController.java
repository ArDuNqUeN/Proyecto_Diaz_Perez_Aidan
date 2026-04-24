package main.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import main.model.Alumno;
import main.model.Usuario;
import main.service.AlumnoService;
import main.service.UsuarioService;

@Controller
@RequestMapping("/panel/alumno")
public class AlumnoController {

    private final UsuarioService usuarioService;
    private final AlumnoService alumnoService;

    public AlumnoController(UsuarioService usuarioService, AlumnoService alumnoService) {
        this.usuarioService = usuarioService;
        this.alumnoService = alumnoService;
    }

    // Panel principal del alumno
    @GetMapping
    public String panelAlumno(Model model) {
        // Obtenemos el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Optional<Alumno> alumnoOpt = alumnoService.findByUsuario(usuario);

            if (alumnoOpt.isPresent()) {
                Alumno alumno = alumnoOpt.get();
                model.addAttribute("alumno", alumno);
                model.addAttribute("practicas", alumno.getPracticas());
                model.addAttribute("registrosHoras", alumno.getRegistrosHoras());
                model.addAttribute("evaluaciones", alumno.getEvaluaciones());
            }
        }
        return "panel-alumno"; // resources/templates/panel-alumno.html
    }

    // Página para registrar horas
    @GetMapping("/registrar-horas")
    public String formularioRegistroHoras(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<Alumno> alumnoOpt = alumnoService.findByUsuario(usuarioOpt.get());
            if (alumnoOpt.isPresent()) {
                model.addAttribute("alumno", alumnoOpt.get());
                model.addAttribute("practicas", alumnoOpt.get().getPracticas());
            }
        }
        return "registro-horas"; // resources/templates/registro-horas.html
    }

    // Página para ver evaluaciones
    @GetMapping("/evaluaciones")
    public String verEvaluaciones(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<Alumno> alumnoOpt = alumnoService.findByUsuario(usuarioOpt.get());
            if (alumnoOpt.isPresent()) {
                model.addAttribute("alumno", alumnoOpt.get());
                model.addAttribute("evaluaciones", alumnoOpt.get().getEvaluaciones());
            }
        }
        return "evaluaciones-alumno"; // resources/templates/evaluaciones-alumno.html
    }
}