package main.controller;

import main.model.*;
import main.repository.*;
import main.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/panel/tutor-centro")
public class TutorCentroController {

    private final UsuarioService usuarioService;
    private final TutorCentroService tutorCentroService;
    private final PracticaRepository practicaRepository;
    private final AlumnoRepository alumnoRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final DocumentoService documentoService;

    public TutorCentroController(UsuarioService usuarioService, 
                                 TutorCentroService tutorCentroService,
                                 PracticaRepository practicaRepository,
                                 AlumnoRepository alumnoRepository,
                                 EvaluacionRepository evaluacionRepository,
                                 DocumentoService documentoService) {
        this.usuarioService = usuarioService;
        this.tutorCentroService = tutorCentroService;
        this.practicaRepository = practicaRepository;
        this.alumnoRepository = alumnoRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.documentoService = documentoService;
    }

    // ========== PANEL PRINCIPAL ==========
    @GetMapping
    public String panelTutorCentro(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorCentro> tutorOpt = tutorCentroService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent()) {
                TutorCentro tutor = tutorOpt.get();
                List<Practica> practicas = practicaRepository.findByTutorCentro(tutor);
                model.addAttribute("tutor", tutor);
                model.addAttribute("practicas", practicas);
                model.addAttribute("alumnos", alumnoRepository.findAll());
                model.addAttribute("evaluaciones", tutor.getEvaluacionesRealizadas());
            }
        }
        return "panel-tutor-centro";
    }

    // ========== EVALUAR ALUMNO ==========
    @GetMapping("/evaluar/{alumnoId}")
    public String formularioEvaluar(@PathVariable Long alumnoId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorCentro> tutorOpt = tutorCentroService.findByUsuario(usuarioOpt.get());
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumnoId);
            if (tutorOpt.isPresent() && alumnoOpt.isPresent()) {
                model.addAttribute("tutor", tutorOpt.get());
                model.addAttribute("alumno", alumnoOpt.get());
                model.addAttribute("practicas", alumnoOpt.get().getPracticas());
                return "evaluar-alumno-centro";
            }
        }
        return "redirect:/panel/tutor-centro";
    }

    @PostMapping("/evaluar")
    public String guardarEvaluacion(@RequestParam Long alumnoId,
                                    @RequestParam(required = false) Long practicaId,
                                    @RequestParam Double nota,
                                    @RequestParam String comentario,
                                    RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorCentro> tutorOpt = tutorCentroService.findByUsuario(usuarioOpt.get());
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumnoId);
            if (tutorOpt.isPresent() && alumnoOpt.isPresent()) {
                Evaluacion evaluacion = new Evaluacion();
                evaluacion.setNota(nota);
                evaluacion.setComentario(comentario);
                evaluacion.setFechaEvaluacion(LocalDateTime.now());
                evaluacion.setTipoEvaluador(TipoEvaluador.TUTOR_CENTRO);
                evaluacion.setAlumno(alumnoOpt.get());
                evaluacion.setTutorCentro(tutorOpt.get());
                if (practicaId != null) {
                    practicaRepository.findById(practicaId).ifPresent(evaluacion::setPractica);
                }
                evaluacionRepository.save(evaluacion);
                redirectAttributes.addFlashAttribute("mensaje", "✅ Evaluación guardada correctamente.");
                return "redirect:/panel/tutor-centro";
            }
        }
        redirectAttributes.addFlashAttribute("error", "❌ Error al guardar la evaluación.");
        return "redirect:/panel/tutor-centro";
    }

    // ========== DOCUMENTOS ==========
    @GetMapping("/documentos")
    public String verDocumentos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorCentro> tutorOpt = tutorCentroService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent()) {
                TutorCentro tutor = tutorOpt.get();
                List<Documento> documentos = documentoService.findByTutorCentro(tutor);
                model.addAttribute("tutor", tutor);
                model.addAttribute("documentos", documentos);
                model.addAttribute("alumnos", alumnoRepository.findAll());
            }
        }
        return "documentos-tutor-centro";
    }

    @PostMapping("/documentos/subir")
    public String subirDocumento(@RequestParam("archivo") MultipartFile archivo,
                                 @RequestParam("tipoDocumento") String tipoDocumento,
                                 @RequestParam(required = false) Long alumnoId,
                                 RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorCentro> tutorOpt = tutorCentroService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent() && !archivo.isEmpty()) {
                try {
                    Alumno alumno = null;
                    if (alumnoId != null) {
                        alumno = alumnoRepository.findById(alumnoId).orElse(null);
                    }
                    documentoService.guardarArchivoTutorCentro(archivo, tutorOpt.get(), tipoDocumento, alumno);
                    redirectAttributes.addFlashAttribute("mensaje", "✅ Documento subido correctamente.");
                    return "redirect:/panel/tutor-centro/documentos";
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("error", "❌ Error al subir el archivo.");
                }
            }
        }
        return "redirect:/panel/tutor-centro/documentos";
    }
}