package main.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import main.model.Alumno;
import main.model.EstadoRegistro;
import main.model.Practica;
import main.model.RegistroHoras;
import main.model.Usuario;
import main.service.AlumnoService;
import main.service.UsuarioService;
import main.repository.PracticaRepository;
import main.repository.RegistroHorasRepository;

@Controller
@RequestMapping("/panel/alumno")
public class AlumnoController {

    private final UsuarioService usuarioService;
    private final AlumnoService alumnoService;
    private final PracticaRepository practicaRepository;
    private final RegistroHorasRepository registroHorasRepository;

    public AlumnoController(UsuarioService usuarioService, 
                           AlumnoService alumnoService,
                           PracticaRepository practicaRepository,
                           RegistroHorasRepository registroHorasRepository) {
        this.usuarioService = usuarioService;
        this.alumnoService = alumnoService;
        this.practicaRepository = practicaRepository;
        this.registroHorasRepository = registroHorasRepository;
    }

    // Panel principal del alumno
    @GetMapping
    public String panelAlumno(Model model) {
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
        return "panel-alumno";
    }

    // Página para registrar horas (GET)
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
        return "registro-horas";
    }

    // 👇 NUEVO: Método POST para guardar el registro de horas
    @PostMapping("/registrar-horas")
    public String guardarRegistroHoras(
            @RequestParam("practicaId") Long practicaId,
            @RequestParam("fecha") LocalDate fecha,
            @RequestParam("horas") int horas,
            @RequestParam("descripcion") String descripcion,
            RedirectAttributes redirectAttributes) {

        // Obtenemos el alumno actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<Alumno> alumnoOpt = alumnoService.findByUsuario(usuarioOpt.get());
            if (alumnoOpt.isPresent()) {
                Alumno alumno = alumnoOpt.get();
                
                // Buscamos la práctica seleccionada
                Optional<Practica> practicaOpt = practicaRepository.findById(practicaId);
                
                if (practicaOpt.isPresent()) {
                    Practica practica = practicaOpt.get();
                    
                    // Creamos el nuevo registro
                    RegistroHoras registro = new RegistroHoras();
                    registro.setFecha(fecha);
                    registro.setHoras(horas);
                    registro.setDescripcion(descripcion);
                    registro.setEstado(EstadoRegistro.PENDIENTE);
                    registro.setAlumno(alumno);
                    registro.setPractica(practica);
                    
                    // Guardamos
                    registroHorasRepository.save(registro);
                    
                    // Mensaje de éxito
                    redirectAttributes.addFlashAttribute("mensaje", "✅ Horas registradas correctamente. Pendiente de validación.");
                    return "redirect:/panel/alumno";
                }
            }
        }
        
        // Si algo falla, volvemos al formulario con mensaje de error
        redirectAttributes.addFlashAttribute("error", "❌ Error al registrar las horas. Inténtalo de nuevo.");
        return "redirect:/panel/alumno/registrar-horas";
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
        return "evaluaciones-alumno";
    }
}