package main.controller;

import main.model.Usuario;
import main.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/panel/admin")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Página principal del panel de administrador
    @GetMapping
    public String panelAdmin(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "panel-admin"; // resources/templates/panel-admin.html
    }

    // Ejemplo de otra página dentro del panel: gestión de usuarios
    @GetMapping("/usuarios")
    public String gestionUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "admin-usuarios"; // resources/templates/admin-usuarios.html
    }
}