package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Corte;
import web.controlevacinacao.repository.queries.corte.CorteQueries;

public interface CorteRepository extends JpaRepository<Corte, Long>, CorteQueries {

}
