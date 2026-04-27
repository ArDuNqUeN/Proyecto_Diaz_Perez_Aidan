package main.controller;

import main.model.*;
import main.repository.*;
import main.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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

    public TutorEmpresaController(UsuarioService usuarioService, 
                                 TutorEmpresaService tutorEmpresaService,
                                 RegistroHorasRepository registroHorasRepository,
                                 PracticaRepository practicaRepository) {
        this.usuarioService = usuarioService;
        this.tutorEmpresaService = tutorEmpresaService;
        this.registroHorasRepository = registroHorasRepository;
        this.practicaRepository = practicaRepository;
    }

    // ========== PANEL PRINCIPAL DEL TUTOR DE EMPRESA ==========
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
                
                // Buscar las prácticas que supervisa este tutor
                List<Practica> practicas = practicaRepository.findByTutorEmpresa(tutor);
                model.addAttribute("practicas", practicas);
                
                // Contar horas pendientes de validar
                int horasPendientes = 0;
                for (Practica p : practicas) {
                    List<RegistroHoras> pendientes = registroHorasRepository.findByPracticaAndEstado(p, EstadoRegistro.PENDIENTE);
                    horasPendientes += pendientes.size();
                }
                model.addAttribute("horasPendientes", horasPendientes);
            }
        }
        return "panel-tutor-empresa";
    }

    // ========== VER HORAS PENDIENTES DE VALIDAR ==========
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
                
                // Buscar todas las prácticas de este tutor
                List<Practica> practicas = practicaRepository.findByTutorEmpresa(tutor);
                
                // Recopilar todos los registros pendientes de esas prácticas
                List<RegistroHoras> pendientes = new ArrayList<>();
                for (Practica p : practicas) {
                    pendientes.addAll(registroHorasRepository.findByPracticaAndEstado(p, EstadoRegistro.PENDIENTE));
                }
                model.addAttribute("registrosPendientes", pendientes);
                
                // También pasamos los ya validados/rechazados
                List<RegistroHoras> historial = new ArrayList<>();
                for (Practica p : practicas) {
                    historial.addAll(registroHorasRepository.findByPractica(p));
                }
                model.addAttribute("historial", historial);
            }
        }
        return "validar-horas";
    }

    // ========== VALIDAR UN REGISTRO DE HORAS ==========
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

    // ========== RECHAZAR UN REGISTRO DE HORAS ==========
    @PostMapping("/rechazar/{id}")
    public String rechazarHoras(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<RegistroHoras> registroOpt = registroHorasRepository.findById(id);
        
        if (registroOpt.isPresent()) {
            RegistroHoras registro = registroOpt.get();
            registro.setEstado(EstadoRegistro.RECHAZADA);
            registroHorasRepository.save(registro);
            redirectAttributes.addFlashAttribute("mensaje", "⚠️ Horas rechazadas. El alumno deberá corregirlas.");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Registro no encontrado.");
        }
        
        return "redirect:/panel/tutor-empresa/validar-horas";
    }
}