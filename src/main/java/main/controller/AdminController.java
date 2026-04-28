package main.controller;

import main.model.*;
import main.repository.*;
import main.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/panel/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final AdministradorRepository administradorRepository;
    private final TutorEmpresaRepository tutorEmpresaRepository;
    private final TutorCentroRepository tutorCentroRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UsuarioRepository usuarioRepository,
                          AlumnoRepository alumnoRepository,
                          AdministradorRepository administradorRepository,
                          TutorEmpresaRepository tutorEmpresaRepository,
                          TutorCentroRepository tutorCentroRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
        this.administradorRepository = administradorRepository;
        this.tutorEmpresaRepository = tutorEmpresaRepository;
        this.tutorCentroRepository = tutorCentroRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========== PANEL PRINCIPAL ==========
    @GetMapping
    public String panelAdmin(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "panel-admin";
    }

    // ========== FORMULARIO CREAR USUARIO ==========
    @GetMapping("/usuarios/nuevo")
    public String formularioCrearUsuario(Model model) {
        return "admin-usuario-form";
    }

    // ========== GUARDAR NUEVO USUARIO ==========
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam String nombre,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String rol,
                                 @RequestParam(required = false) String matricula,
                                 @RequestParam(required = false) String apellidos,
                                 @RequestParam(required = false) String telefono,
                                 @RequestParam(required = false) String cargo,
                                 @RequestParam(required = false) String departamento,
                                 @RequestParam(required = false) String despacho,
                                 RedirectAttributes redirectAttributes) {

        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "❌ El email ya está registrado.");
            return "redirect:/panel/admin/usuarios/nuevo";
        }

        // Crear el usuario base
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuario = usuarioRepository.save(usuario);

        // Crear el perfil según el rol
        switch (rol) {
            case "ROLE_ALUMNO":
                Alumno alumno = new Alumno();
                alumno.setUsuario(usuario);
                alumno.setMatricula(matricula != null ? matricula : "SIN-MATRICULA");
                alumno.setApellidos(apellidos);
                alumno.setTelefono(telefono);
                alumnoRepository.save(alumno);
                break;

            case "ROLE_TUTOR_EMPRESA":
                TutorEmpresa tutorEmpresa = new TutorEmpresa();
                tutorEmpresa.setUsuario(usuario);
                tutorEmpresa.setCargo(cargo);
                tutorEmpresa.setTelefono(telefono);
                tutorEmpresaRepository.save(tutorEmpresa);
                break;

            case "ROLE_TUTOR_CENTRO":
                TutorCentro tutorCentro = new TutorCentro();
                tutorCentro.setUsuario(usuario);
                tutorCentro.setDepartamento(departamento);
                tutorCentro.setDespacho(despacho);
                tutorCentroRepository.save(tutorCentro);
                break;

            case "ROLE_ADMIN":
                Administrador admin = new Administrador();
                admin.setUsuario(usuario);
                admin.setDepartamento(departamento != null ? departamento : "General");
                administradorRepository.save(admin);
                break;
        }

        redirectAttributes.addFlashAttribute("mensaje", "✅ Usuario '" + nombre + "' creado correctamente.");
        return "redirect:/panel/admin";
    }

    // ========== FORMULARIO EDITAR USUARIO ==========
    @GetMapping("/usuarios/editar/{id}")
    public String formularioEditarUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            return "admin-usuario-editar";
        }
        redirectAttributes.addFlashAttribute("error", "❌ Usuario no encontrado.");
        return "redirect:/panel/admin";
    }

    // ========== GUARDAR EDICIÓN ==========
    @PostMapping("/usuarios/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                    @RequestParam String nombre,
                                    @RequestParam String email,
                                    @RequestParam(required = false) String password,
                                    @RequestParam String rol,
                                    RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setRol(rol);

            // Solo actualizar contraseña si se proporciona una nueva
            if (password != null && !password.isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(password));
            }

            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Usuario actualizado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Usuario no encontrado.");
        }
        return "redirect:/panel/admin";
    }

    // ========== ACTIVAR/DESACTIVAR USUARIO ==========
    @PostMapping("/usuarios/toggle/{id}")
    public String toggleUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(!usuario.isActivo());
            usuarioRepository.save(usuario);
            String estado = usuario.isActivo() ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", "✅ Usuario " + estado + " correctamente.");
        }
        return "redirect:/panel/admin";
    }

    // ========== ELIMINAR USUARIO ==========
    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            String nombre = usuarioOpt.get().getNombre();
            usuarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Usuario '" + nombre + "' eliminado.");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Usuario no encontrado.");
        }
        return "redirect:/panel/admin";
    }
}