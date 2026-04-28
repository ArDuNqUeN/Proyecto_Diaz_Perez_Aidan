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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/panel/tutor-empresa")
public class TutorEmpresaController {

    private final UsuarioService usuarioService;
    private final TutorEmpresaService tutorEmpresaService;
    private final RegistroHorasRepository registroHorasRepository;
    private final PracticaRepository practicaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final AlumnoRepository alumnoRepository;
    private final DocumentoService documentoService;

    public TutorEmpresaController(UsuarioService usuarioService, 
                                 TutorEmpresaService tutorEmpresaService,
                                 RegistroHorasRepository registroHorasRepository,
                                 PracticaRepository practicaRepository,
                                 EvaluacionRepository evaluacionRepository,
                                 AlumnoRepository alumnoRepository,
                                 DocumentoService documentoService) {
        this.usuarioService = usuarioService;
        this.tutorEmpresaService = tutorEmpresaService;
        this.registroHorasRepository = registroHorasRepository;
        this.practicaRepository = practicaRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.alumnoRepository = alumnoRepository;
        this.documentoService = documentoService;
    }

    // ========== PANEL PRINCIPAL ==========
    @GetMapping
    public String panelTutorEmpresa(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorEmpresa> tutorOpt = tutorEmpresaService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent()) {
                TutorEmpresa tutor = tutorOpt.get();
                model.addAttribute("tutor", tutor);
                model.addAttribute("empresa", tutor.getEmpresa());
                
                List<Practica> practicas = practicaRepository.findByTutorEmpresa(tutor);
                model.addAttribute("practicas", practicas);
                
                int horasPendientes = 0;
                for (Practica p : practicas) {
                    List<RegistroHoras> pendientes = registroHorasRepository.findByPracticaAndEstado(p, EstadoRegistro.PENDIENTE);
                    horasPendientes += pendientes.size();
                }
                model.addAttribute("horasPendientes", horasPendientes);
                model.addAttribute("evaluaciones", tutor.getEvaluacionesRealizadas());
            }
        }
        return "panel-tutor-empresa";
    }

    // ========== VALIDAR HORAS ==========
    @GetMapping("/validar-horas")
    public String listarHorasPendientes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorEmpresa> tutorOpt = tutorEmpresaService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent()) {
                TutorEmpresa tutor = tutorOpt.get();
                model.addAttribute("tutor", tutor);
                
                List<Practica> practicas = practicaRepository.findByTutorEmpresa(tutor);
                
                List<RegistroHoras> pendientes = new ArrayList<>();
                for (Practica p : practicas) {
                    pendientes.addAll(registroHorasRepository.findByPracticaAndEstado(p, EstadoRegistro.PENDIENTE));
                }
                model.addAttribute("registrosPendientes", pendientes);
                
                List<RegistroHoras> historial = new ArrayList<>();
                for (Practica p : practicas) {
                    historial.addAll(registroHorasRepository.findByPractica(p));
                }
                model.addAttribute("historial", historial);
            }
        }
        return "validar-horas";
    }

    @PostMapping("/validar/{id}")
    public String validarHoras(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<RegistroHoras> registroOpt = registroHorasRepository.findById(id);
        if (registroOpt.isPresent()) {
            RegistroHoras registro = registroOpt.get();
            registro.setEstado(EstadoRegistro.VALIDADA);
            registroHorasRepository.save(registro);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Horas validadas correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Registro no encontrado.");
        }
        return "redirect:/panel/tutor-empresa/validar-horas";
    }

    @PostMapping("/rechazar/{id}")
    public String rechazarHoras(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<RegistroHoras> registroOpt = registroHorasRepository.findById(id);
        if (registroOpt.isPresent()) {
            RegistroHoras registro = registroOpt.get();
            registro.setEstado(EstadoRegistro.RECHAZADA);
            registroHorasRepository.save(registro);
            redirectAttributes.addFlashAttribute("mensaje", "⚠️ Horas rechazadas.");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Registro no encontrado.");
        }
        return "redirect:/panel/tutor-empresa/validar-horas";
    }

    // ========== EVALUAR ==========
    @GetMapping("/evaluar/{alumnoId}")
    public String formularioEvaluar(@PathVariable Long alumnoId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorEmpresa> tutorOpt = tutorEmpresaService.findByUsuario(usuarioOpt.get());
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumnoId);
            if (tutorOpt.isPresent() && alumnoOpt.isPresent()) {
                model.addAttribute("tutor", tutorOpt.get());
                model.addAttribute("alumno", alumnoOpt.get());
                model.addAttribute("practicas", alumnoOpt.get().getPracticas());
                return "evaluar-alumno";
            }
        }
        return "redirect:/panel/tutor-empresa";
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
            Optional<TutorEmpresa> tutorOpt = tutorEmpresaService.findByUsuario(usuarioOpt.get());
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumnoId);
            if (tutorOpt.isPresent() && alumnoOpt.isPresent()) {
                Evaluacion evaluacion = new Evaluacion();
                evaluacion.setNota(nota);
                evaluacion.setComentario(comentario);
                evaluacion.setFechaEvaluacion(LocalDateTime.now());
                evaluacion.setTipoEvaluador(TipoEvaluador.TUTOR_EMPRESA);
                evaluacion.setAlumno(alumnoOpt.get());
                evaluacion.setTutorEmpresa(tutorOpt.get());
                if (practicaId != null) {
                    practicaRepository.findById(practicaId).ifPresent(evaluacion::setPractica);
                }
                evaluacionRepository.save(evaluacion);
                redirectAttributes.addFlashAttribute("mensaje", "✅ Evaluación guardada correctamente.");
                return "redirect:/panel/tutor-empresa";
            }
        }
        redirectAttributes.addFlashAttribute("error", "❌ Error al guardar la evaluación.");
        return "redirect:/panel/tutor-empresa";
    }

    // ========== DOCUMENTOS ==========
    @GetMapping("/documentos")
    public String verDocumentos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorEmpresa> tutorOpt = tutorEmpresaService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent()) {
                TutorEmpresa tutor = tutorOpt.get();
                List<Documento> documentos = documentoService.findByTutorEmpresa(tutor);
                model.addAttribute("tutor", tutor);
                model.addAttribute("documentos", documentos);
                model.addAttribute("alumnos", alumnoRepository.findAll());
            }
        }
        return "documentos-tutor-empresa";
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
            Optional<TutorEmpresa> tutorOpt = tutorEmpresaService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent() && !archivo.isEmpty()) {
                try {
                    Alumno alumno = null;
                    if (alumnoId != null) {
                        alumno = alumnoRepository.findById(alumnoId).orElse(null);
                    }
                    documentoService.guardarArchivoTutorEmpresa(archivo, tutorOpt.get(), tipoDocumento, alumno);
                    redirectAttributes.addFlashAttribute("mensaje", "✅ Documento subido correctamente.");
                    return "redirect:/panel/tutor-empresa/documentos";
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("error", "❌ Error al subir el archivo.");
                }
            }
        }
        return "redirect:/panel/tutor-empresa/documentos";
    }
}