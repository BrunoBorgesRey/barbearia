package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Barbeador;
import web.controlevacinacao.repository.BarbeadorRepository;

@Service
@Transactional
public class BarbeadorService {

    private BarbeadorRepository barbeadorRepository;

    public BarbeadorService(BarbeadorRepository barbeadorRepository) {
        this.barbeadorRepository = barbeadorRepository;
    }

    public void salvar(Barbeador barbeador) {
        barbeadorRepository.save(barbeador);
    }

    public void alterar(Barbeador barbeador) {
        barbeadorRepository.save(barbeador);
    }
}
