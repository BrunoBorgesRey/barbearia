package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Corte;
import web.controlevacinacao.repository.CorteRepository;

@Service
@Transactional
public class CorteService {

    private CorteRepository corteRepository;

    public CorteService(CorteRepository corteRepository) {
        this.corteRepository = corteRepository;
    }

    public void salvar(Corte corte) {
        corteRepository.save(corte);
    }

    public void alterar(Corte corte) {
        corteRepository.save(corte);
    }

    public void remover(Corte corte) {
        corteRepository.delete(corte);
    }
}