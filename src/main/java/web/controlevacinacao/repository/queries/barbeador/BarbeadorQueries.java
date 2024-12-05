package web.controlevacinacao.repository.queries.barbeador;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.BarbeadorFilter;
import web.controlevacinacao.model.Barbeador;

public interface BarbeadorQueries {

    Page<Barbeador> pesquisar(BarbeadorFilter filtro, Pageable pageable);
}
