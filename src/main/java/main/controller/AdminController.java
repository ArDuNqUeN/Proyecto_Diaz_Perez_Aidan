package main.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import main.model.*;
import main.repository.*;

@Controller
@RequestMapping("/panel/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final AdministradorRepository administradorRepository;
    private final TutorEmpresaRepository tutorEmpresaRepository;
    private final TutorCentroRepository tutorCentroRepository;
    private final EmpresaRepository empresaRepository;
    private final PracticaRepository practicaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final RegistroHorasRepository registroHorasRepository;
    private final DocumentoRepository documentoRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UsuarioRepository usuarioRepository,
                          AlumnoRepository alumnoRepository,
                          AdministradorRepository administradorRepository,
                          TutorEmpresaRepository tutorEmpresaRepository,
                          TutorCentroRepository tutorCentroRepository,
                          EmpresaRepository empresaRepository,
                          PracticaRepository practicaRepository,
                          EvaluacionRepository evaluacionRepository,
                          RegistroHorasRepository registroHorasRepository,
                          DocumentoRepository documentoRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
        this.administradorRepository = administradorRepository;
        this.tutorEmpresaRepository = tutorEmpresaRepository;
        this.tutorCentroRepository = tutorCentroRepository;
        this.empresaRepository = empresaRepository;
        this.practicaRepository = practicaRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.registroHorasRepository = registroHorasRepository;
        this.documentoRepository = documentoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String panelAdmin(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalAlumnos", alumnoRepository.count());
        model.addAttribute("totalEmpresas", empresaRepository.count());
        model.addAttribute("totalPracticas", practicaRepository.count());
        model.addAttribute("totalEvaluaciones", evaluacionRepository.count());
        model.addAttribute("horasPendientes", registroHorasRepository.count());
        return "panel-admin";
    }

    @GetMapping("/usuarios/nuevo")
    public String formularioCrearUsuario() { return "admin-usuario-form"; }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam String nombre, @RequestParam String email,
                                 @RequestParam String password, @RequestParam String rol,
                                 @RequestParam(required = false) String matricula,
                                 @RequestParam(required = false) String apellidos,
                                 @RequestParam(required = false) String telefono,
                                 @RequestParam(required = false) String cargo,
                                 @RequestParam(required = false) String departamento,
                                 @RequestParam(required = false) String despacho,
                                 RedirectAttributes ra) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            ra.addFlashAttribute("error", "El email ya está registrado.");
            return "redirect:/panel/admin/usuarios/nuevo";
        }
        Usuario u = new Usuario();
        u.setNombre(nombre); u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRol(rol); u.setActivo(true);
        u = usuarioRepository.save(u);
        switch (rol) {
            case "ROLE_ALUMNO":
                Alumno a = new Alumno(); a.setUsuario(u);
                a.setMatricula(matricula != null ? matricula : "SIN-MATRICULA");
                a.setApellidos(apellidos); a.setTelefono(telefono);
                alumnoRepository.save(a); break;
            case "ROLE_TUTOR_EMPRESA":
                TutorEmpresa te = new TutorEmpresa(); te.setUsuario(u);
                te.setCargo(cargo); te.setTelefono(telefono);
                tutorEmpresaRepository.save(te); break;
            case "ROLE_TUTOR_CENTRO":
                TutorCentro tc = new TutorCentro(); tc.setUsuario(u);
                tc.setDepartamento(departamento); tc.setDespacho(despacho);
                tutorCentroRepository.save(tc); break;
            case "ROLE_ADMIN":
                Administrador ad = new Administrador(); ad.setUsuario(u);
                ad.setDepartamento(departamento != null ? departamento : "General");
                administradorRepository.save(ad); break;
        }
        ra.addFlashAttribute("mensaje", "Usuario '" + nombre + "' creado.");
        return "redirect:/panel/admin";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model m, RedirectAttributes ra) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isPresent()) { m.addAttribute("usuario", opt.get()); return "admin-usuario-editar"; }
        ra.addFlashAttribute("error", "Usuario no encontrado."); return "redirect:/panel/admin";
    }

    @PostMapping("/usuarios/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @RequestParam String nombre,
                                    @RequestParam String email, @RequestParam(required = false) String password,
                                    @RequestParam String rol, RedirectAttributes ra) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isPresent()) {
            Usuario u = opt.get(); u.setNombre(nombre); u.setEmail(email); u.setRol(rol);
            if (password != null && !password.isEmpty()) u.setPassword(passwordEncoder.encode(password));
            usuarioRepository.save(u);
            ra.addFlashAttribute("mensaje", "Usuario actualizado.");
        } else ra.addFlashAttribute("error", "Usuario no encontrado.");
        return "redirect:/panel/admin";
    }

    @PostMapping("/usuarios/toggle/{id}")
    public String toggleUsuario(@PathVariable Long id, RedirectAttributes ra) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isPresent()) {
            Usuario u = opt.get(); u.setActivo(!u.isActivo()); usuarioRepository.save(u);
            ra.addFlashAttribute("mensaje", "Usuario " + (u.isActivo() ? "activado" : "desactivado") + ".");
        }
        return "redirect:/panel/admin";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes ra) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isPresent()) { String n = opt.get().getNombre(); usuarioRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Usuario '" + n + "' eliminado."); }
        else ra.addFlashAttribute("error", "Usuario no encontrado.");
        return "redirect:/panel/admin";
    }

    @GetMapping("/practicas")
    public String listarPracticas(Model m) { m.addAttribute("practicas", practicaRepository.findAll()); return "admin-practicas"; }

    @GetMapping("/practicas/nueva")
    public String nuevaPractica(Model m) {
        m.addAttribute("alumnos", alumnoRepository.findAll());
        m.addAttribute("empresas", empresaRepository.findAll());
        m.addAttribute("tutoresEmpresa", tutorEmpresaRepository.findAll());
        m.addAttribute("tutoresCentro", tutorCentroRepository.findAll());
        return "admin-practica-form";
    }

    @PostMapping("/practicas/guardar")
    public String guardarPractica(@RequestParam String titulo, @RequestParam String descripcion,
                                  @RequestParam String fechaInicio, @RequestParam String fechaFin,
                                  @RequestParam int horasPrevistas, @RequestParam Long alumnoId,
                                  @RequestParam Long empresaId, @RequestParam Long tutorEmpresaId,
                                  @RequestParam Long tutorCentroId, RedirectAttributes ra) {
        Practica p = new Practica();
        p.setTitulo(titulo); p.setDescripcion(descripcion);
        p.setFechaInicio(LocalDate.parse(fechaInicio)); p.setFechaFin(LocalDate.parse(fechaFin));
        p.setHorasPrevistas(horasPrevistas); p.setEstado(EstadoPractica.ACTIVA);
        p.setAlumno(alumnoRepository.findById(alumnoId).orElse(null));
        p.setEmpresa(empresaRepository.findById(empresaId).orElse(null));
        p.setTutorEmpresa(tutorEmpresaRepository.findById(tutorEmpresaId).orElse(null));
        p.setTutorCentro(tutorCentroRepository.findById(tutorCentroId).orElse(null));
        practicaRepository.save(p);
        ra.addFlashAttribute("mensaje", "Práctica asignada."); return "redirect:/panel/admin/practicas";
    }

    @PostMapping("/practicas/eliminar/{id}")
    public String eliminarPractica(@PathVariable Long id, RedirectAttributes ra) {
        if (practicaRepository.findById(id).isPresent()) { practicaRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Práctica eliminada."); }
        else ra.addFlashAttribute("error", "No encontrada.");
        return "redirect:/panel/admin/practicas";
    }

    @GetMapping("/practicas/editar/{id}")
    public String editarPractica(@PathVariable Long id, Model m, RedirectAttributes ra) {
        Optional<Practica> opt = practicaRepository.findById(id);
        if (opt.isPresent()) {
            m.addAttribute("practica", opt.get());
            m.addAttribute("alumnos", alumnoRepository.findAll());
            m.addAttribute("empresas", empresaRepository.findAll());
            m.addAttribute("tutoresEmpresa", tutorEmpresaRepository.findAll());
            m.addAttribute("tutoresCentro", tutorCentroRepository.findAll());
            return "admin-practica-editar";
        }
        ra.addFlashAttribute("error", "No encontrada."); return "redirect:/panel/admin/practicas";
    }

    @PostMapping("/practicas/editar/{id}")
    public String actualizarPractica(@PathVariable Long id, @RequestParam String titulo, @RequestParam String descripcion,
                                     @RequestParam String fechaInicio, @RequestParam String fechaFin,
                                     @RequestParam int horasPrevistas, @RequestParam Long alumnoId,
                                     @RequestParam Long empresaId, @RequestParam Long tutorEmpresaId,
                                     @RequestParam Long tutorCentroId, RedirectAttributes ra) {
        Optional<Practica> opt = practicaRepository.findById(id);
        if (opt.isPresent()) {
            Practica p = opt.get(); p.setTitulo(titulo); p.setDescripcion(descripcion);
            p.setFechaInicio(LocalDate.parse(fechaInicio)); p.setFechaFin(LocalDate.parse(fechaFin));
            p.setHorasPrevistas(horasPrevistas);
            p.setAlumno(alumnoRepository.findById(alumnoId).orElse(null));
            p.setEmpresa(empresaRepository.findById(empresaId).orElse(null));
            p.setTutorEmpresa(tutorEmpresaRepository.findById(tutorEmpresaId).orElse(null));
            p.setTutorCentro(tutorCentroRepository.findById(tutorCentroId).orElse(null));
            practicaRepository.save(p);
            ra.addFlashAttribute("mensaje", "Práctica actualizada.");
        } else ra.addFlashAttribute("error", "No encontrada.");
        return "redirect:/panel/admin/practicas";
    }

    @PostMapping("/practicas/finalizar/{id}")
    public String finalizarPractica(@PathVariable Long id, RedirectAttributes ra) {
        Optional<Practica> opt = practicaRepository.findById(id);
        if (opt.isPresent()) { opt.get().setEstado(EstadoPractica.FINALIZADA); practicaRepository.save(opt.get());
            ra.addFlashAttribute("mensaje", "Práctica finalizada."); }
        return "redirect:/panel/admin/practicas";
    }

    @PostMapping("/practicas/cancelar/{id}")
    public String cancelarPractica(@PathVariable Long id, RedirectAttributes ra) {
        Optional<Practica> opt = practicaRepository.findById(id);
        if (opt.isPresent()) { opt.get().setEstado(EstadoPractica.CANCELADA); practicaRepository.save(opt.get());
            ra.addFlashAttribute("mensaje", "Práctica cancelada."); }
        return "redirect:/panel/admin/practicas";
    }

    @GetMapping("/empresas")
    public String listarEmpresas(Model m) { m.addAttribute("empresas", empresaRepository.findAll()); return "admin-empresas"; }

    @GetMapping("/empresas/nueva")
    public String nuevaEmpresa() { return "admin-empresa-form"; }

    @PostMapping("/empresas/guardar")
    public String guardarEmpresa(@RequestParam String nombre, @RequestParam String direccion,
                                 @RequestParam String sector, @RequestParam String cif, RedirectAttributes ra) {
        if (empresaRepository.findByCif(cif).isPresent()) { ra.addFlashAttribute("error", "CIF ya registrado."); return "redirect:/panel/admin/empresas/nueva"; }
        Empresa e = new Empresa(); e.setNombre(nombre); e.setDireccion(direccion); e.setSector(sector); e.setCif(cif);
        empresaRepository.save(e); ra.addFlashAttribute("mensaje", "Empresa creada."); return "redirect:/panel/admin/empresas";
    }

    @GetMapping("/empresas/editar/{id}")
    public String editarEmpresa(@PathVariable Long id, Model m, RedirectAttributes ra) {
        Optional<Empresa> opt = empresaRepository.findById(id);
        if (opt.isPresent()) { m.addAttribute("empresa", opt.get()); return "admin-empresa-editar"; }
        ra.addFlashAttribute("error", "No encontrada."); return "redirect:/panel/admin/empresas";
    }

    @PostMapping("/empresas/editar/{id}")
    public String actualizarEmpresa(@PathVariable Long id, @RequestParam String nombre, @RequestParam String direccion,
                                    @RequestParam String sector, @RequestParam String cif, RedirectAttributes ra) {
        Optional<Empresa> opt = empresaRepository.findById(id);
        if (opt.isPresent()) { Empresa e = opt.get(); e.setNombre(nombre); e.setDireccion(direccion); e.setSector(sector); e.setCif(cif);
            empresaRepository.save(e); ra.addFlashAttribute("mensaje", "Empresa actualizada."); }
        else ra.addFlashAttribute("error", "No encontrada.");
        return "redirect:/panel/admin/empresas";
    }

    @PostMapping("/empresas/eliminar/{id}")
    public String eliminarEmpresa(@PathVariable Long id, RedirectAttributes ra) {
        try { empresaRepository.deleteById(id); ra.addFlashAttribute("mensaje", "Empresa eliminada."); }
        catch (Exception e) { ra.addFlashAttribute("error", "No se puede eliminar."); }
        return "redirect:/panel/admin/empresas";
    }

    @GetMapping("/evaluaciones")
    public String listarEvaluaciones(Model m) { m.addAttribute("evaluaciones", evaluacionRepository.findAll()); return "admin-evaluaciones"; }

    @GetMapping("/documentos")
    public String listarDocumentos(Model m) { m.addAttribute("documentos", documentoRepository.findAll()); return "admin-documentos"; }
}