package main.controller;

import main.model.*;
import main.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@RequestMapping("/panel/tutor-empresa")
public class TutorEmpresaController {

    private final UsuarioService usuarioService;
    private final TutorEmpresaService tutorEmpresaService;

    public TutorEmpresaController(UsuarioService usuarioService, TutorEmpresaService tutorEmpresaService) {
        this.usuarioService = usuarioService;
        this.tutorEmpresaService = tutorEmpresaService;
    }

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
                model.addAttribute("evaluaciones", tutor.getEvaluacionesRealizadas());
            }
        }
        return "panel-tutor-empresa";
    }
}