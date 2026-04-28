package main.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import main.model.Alumno;
import main.model.Documento;
import main.model.EstadoRegistro;
import main.model.Practica;
import main.model.RegistroHoras;
import main.model.Usuario;
import main.repository.PracticaRepository;
import main.repository.RegistroHorasRepository;
import main.service.AlumnoService;
import main.service.DocumentoService;
import main.service.UsuarioService;

@Controller
@RequestMapping("/panel/alumno")
public class AlumnoController {

    private final UsuarioService usuarioService;
    private final AlumnoService alumnoService;
    private final PracticaRepository practicaRepository;
    private final RegistroHorasRepository registroHorasRepository;
    private final DocumentoService documentoService;

    public AlumnoController(UsuarioService usuarioService, 
                           AlumnoService alumnoService,
                           PracticaRepository practicaRepository,
                           RegistroHorasRepository registroHorasRepository,
                           DocumentoService documentoService) {
        this.usuarioService = usuarioService;
        this.alumnoService = alumnoService;
        this.practicaRepository = practicaRepository;
        this.registroHorasRepository = registroHorasRepository;
        this.documentoService = documentoService;
    }

    // ========== PANEL PRINCIPAL DEL ALUMNO ==========
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

    // ========== FORMULARIO REGISTRO DE HORAS (GET) ==========
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

    // ========== GUARDAR REGISTRO DE HORAS (POST) ==========
    @PostMapping("/registrar-horas")
    public String guardarRegistroHoras(
            @RequestParam("practicaId") Long practicaId,
            @RequestParam("fecha") LocalDate fecha,
            @RequestParam("horas") int horas,
            @RequestParam("descripcion") String descripcion,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<Alumno> alumnoOpt = alumnoService.findByUsuario(usuarioOpt.get());
            if (alumnoOpt.isPresent()) {
                Alumno alumno = alumnoOpt.get();
                Optional<Practica> practicaOpt = practicaRepository.findById(practicaId);

                if (practicaOpt.isPresent()) {
                    Practica practica = practicaOpt.get();

                    RegistroHoras registro = new RegistroHoras();
                    registro.setFecha(fecha);
                    registro.setHoras(horas);
                    registro.setDescripcion(descripcion);
                    registro.setEstado(EstadoRegistro.PENDIENTE);
                    registro.setAlumno(alumno);
                    registro.setPractica(practica);

                    registroHorasRepository.save(registro);
                    redirectAttributes.addFlashAttribute("mensaje", "✅ Horas registradas correctamente. Pendiente de validación.");
                    return "redirect:/panel/alumno";
                }
            }
        }

        redirectAttributes.addFlashAttribute("error", "❌ Error al registrar las horas. Inténtalo de nuevo.");
        return "redirect:/panel/alumno/registrar-horas";
    }

    // ========== VER EVALUACIONES ==========
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

    // ========== VER DOCUMENTOS ==========
    @GetMapping("/documentos")
    public String verDocumentos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<Alumno> alumnoOpt = alumnoService.findByUsuario(usuarioOpt.get());
            if (alumnoOpt.isPresent()) {
                Alumno alumno = alumnoOpt.get();
                List<Documento> documentos = documentoService.findByAlumno(alumno);
                model.addAttribute("alumno", alumno);
                model.addAttribute("documentos", documentos);
            }
        }
        return "documentos-alumno";
    }

    // ========== SUBIR DOCUMENTO (POST) ==========
    @PostMapping("/documentos/subir")
    public String subirDocumento(@RequestParam("archivo") MultipartFile archivo,
                                 @RequestParam("tipoDocumento") String tipoDocumento,
                                 RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<Alumno> alumnoOpt = alumnoService.findByUsuario(usuarioOpt.get());
            if (alumnoOpt.isPresent() && !archivo.isEmpty()) {
                try {
                    documentoService.guardarArchivo(archivo, alumnoOpt.get(), tipoDocumento);
                    redirectAttributes.addFlashAttribute("mensaje", "✅ Documento subido correctamente.");
                    return "redirect:/panel/alumno/documentos";
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("error", "❌ Error al subir el archivo.");
                }
            }
        }
        return "redirect:/panel/alumno/documentos";
    }
}