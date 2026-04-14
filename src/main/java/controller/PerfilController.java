package controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import model.Usuario;
import repository.UsuarioRepository;

@Controller
public class PerfilController {

    @Autowired
    private UsuarioRepository repo;

    @GetMapping("/perfil")
    public String perfil(Model model) {

        // 👇 usuario fijo para demo
        Usuario u = repo.findByUsername("demo");

        model.addAttribute("usuario", u);
        return "perfil";
    }

    @PostMapping("/perfil/foto")
    public String subirFoto(@RequestParam("file") MultipartFile file) throws Exception {

        String ruta = "uploads/" + file.getOriginalFilename();
        file.transferTo(new File(ruta));

        Usuario u = repo.findByUsername("demo");
        u.setFotoPerfil(ruta);
        repo.save(u);

        return "redirect:/perfil";
    }
}