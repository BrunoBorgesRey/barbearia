package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Barbeador;
import web.controlevacinacao.repository.queries.barbeador.BarbeadorQueries;

public interface BarbeadorRepository extends JpaRepository<Barbeador, Long>, BarbeadorQueries {

}
