package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import model.Usuario;
import repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", repo.findAll());
        return "usuarios/list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    @PostMapping
    public String guardar(@ModelAttribute Usuario usuario) {
        repo.save(usuario);
        return "redirect:/usuarios";
    }
}