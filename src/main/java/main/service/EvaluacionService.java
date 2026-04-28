package main.service;

import main.model.Evaluacion;
import main.repository.EvaluacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository) {
        this.evaluacionRepository = evaluacionRepository;
    }

    public Evaluacion guardar(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    public Optional<Evaluacion> findById(Long id) {
        return evaluacionRepository.findById(id);
    }

    public List<Evaluacion> listarTodas() {
        return evaluacionRepository.findAll();
    }
}