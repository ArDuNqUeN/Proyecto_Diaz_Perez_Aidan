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
@RequestMapping("/panel/tutor-centro")
public class TutorCentroController {

    private final UsuarioService usuarioService;
    private final TutorCentroService tutorCentroService;

    public TutorCentroController(UsuarioService usuarioService, TutorCentroService tutorCentroService) {
        this.usuarioService = usuarioService;
        this.tutorCentroService = tutorCentroService;
    }

    @GetMapping
    public String panelTutorCentro(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Optional<TutorCentro> tutorOpt = tutorCentroService.findByUsuario(usuarioOpt.get());
            if (tutorOpt.isPresent()) {
                TutorCentro tutor = tutorOpt.get();
                model.addAttribute("tutor", tutor);
                model.addAttribute("evaluaciones", tutor.getEvaluacionesRealizadas());
                model.addAttribute("informes", tutor.getInformesGenerados());
            }
        }
        return "panel-tutor-centro";
    }
}