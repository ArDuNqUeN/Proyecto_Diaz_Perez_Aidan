package main.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import main.model.Alumno;
import main.model.Documento;
import main.model.TutorEmpresa;
import main.model.TutorCentro;
import main.repository.DocumentoRepository;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final Path uploadDir;

    public DocumentoService(DocumentoRepository documentoRepository,
                           @Value("${app.upload.dir:./uploads}") String uploadDir) {
        this.documentoRepository = documentoRepository;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.uploadDir);
            System.out.println("📁 Carpeta de uploads creada en: " + this.uploadDir.toString());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de uploads en: " + this.uploadDir, e);
        }
    }

    // ========== GUARDAR ARCHIVO DE ALUMNO ==========
    public Documento guardarArchivo(MultipartFile archivo, Alumno alumno, String tipoDocumento) throws IOException {
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreUnico = UUID.randomUUID().toString() + extension;
        
        Path rutaCompleta = uploadDir.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
        
        Documento documento = new Documento();
        documento.setNombreOriginal(nombreOriginal);
        documento.setNombreGuardado(nombreUnico);
        documento.setRutaArchivo(rutaCompleta.toString());
        documento.setTipoDocumento(tipoDocumento);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setAlumno(alumno);
        documento.setTamaño(archivo.getSize());
        
        return documentoRepository.save(documento);
    }

    // ========== GUARDAR ARCHIVO DE TUTOR EMPRESA ==========
    public Documento guardarArchivoTutorEmpresa(MultipartFile archivo, TutorEmpresa tutor, 
                                                String tipoDocumento, Alumno alumno) throws IOException {
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreUnico = UUID.randomUUID().toString() + extension;
        
        Path rutaCompleta = uploadDir.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
        
        Documento documento = new Documento();
        documento.setNombreOriginal(nombreOriginal);
        documento.setNombreGuardado(nombreUnico);
        documento.setRutaArchivo(rutaCompleta.toString());
        documento.setTipoDocumento(tipoDocumento);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setTutorEmpresa(tutor);
        if (alumno != null) {
            documento.setAlumno(alumno);
        }
        documento.setTamaño(archivo.getSize());
        
        return documentoRepository.save(documento);
    }

    // ========== GUARDAR ARCHIVO DE TUTOR CENTRO ==========
    public Documento guardarArchivoTutorCentro(MultipartFile archivo, TutorCentro tutor, 
                                               String tipoDocumento, Alumno alumno) throws IOException {
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreUnico = UUID.randomUUID().toString() + extension;
        
        Path rutaCompleta = uploadDir.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
        
        Documento documento = new Documento();
        documento.setNombreOriginal(nombreOriginal);
        documento.setNombreGuardado(nombreUnico);
        documento.setRutaArchivo(rutaCompleta.toString());
        documento.setTipoDocumento(tipoDocumento);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setTutorCentro(tutor);
        if (alumno != null) {
            documento.setAlumno(alumno);
        }
        documento.setTamaño(archivo.getSize());
        
        return documentoRepository.save(documento);
    }

    // ========== BUSCAR DOCUMENTOS ==========
    public List<Documento> findByAlumno(Alumno alumno) {
        return documentoRepository.findByAlumno(alumno);
    }

    public List<Documento> findByTutorEmpresa(TutorEmpresa tutor) {
        return documentoRepository.findByTutorEmpresa(tutor);
    }

    public List<Documento> findByTutorCentro(TutorCentro tutor) {
        return documentoRepository.findByTutorCentro(tutor);
    }

    public Optional<Documento> findById(Long id) {
        return documentoRepository.findById(id);
    }

    // ========== ELIMINAR DOCUMENTO ==========
    public void eliminar(Long id) {
        Optional<Documento> docOpt = documentoRepository.findById(id);
        if (docOpt.isPresent()) {
            Documento doc = docOpt.get();
            try {
                Files.deleteIfExists(Paths.get(doc.getRutaArchivo()));
            } catch (IOException e) {
                // Si no se puede borrar, continuamos
            }
            documentoRepository.deleteById(id);
        }
    }
}