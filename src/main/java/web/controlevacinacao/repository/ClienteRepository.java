package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.repository.queries.vacina.ClienteQueries;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteQueries {

}