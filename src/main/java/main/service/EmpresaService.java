package main.service;

import main.model.Empresa;
import main.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    /**
     * Lista todas las empresas registradas.
     */
    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    /**
     * Busca una empresa por su CIF.
     */
    public Optional<Empresa> findByCif(String cif) {
        return empresaRepository.findByCif(cif);
    }

    /**
     * Busca una empresa por su ID.
     */
    public Optional<Empresa> findById(Long id) {
        return empresaRepository.findById(id);
    }

    /**
     * Guarda una empresa (nueva o actualizada).
     */
    public Empresa guardar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    /**
     * Elimina una empresa por su ID.
     */
    public void eliminar(Long id) {
        empresaRepository.deleteById(id);
    }
}