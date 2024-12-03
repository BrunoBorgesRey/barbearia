package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void salvar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public void alterar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public void remover(Cliente cliente) {
        clienteRepository.delete(cliente);
    }
}
