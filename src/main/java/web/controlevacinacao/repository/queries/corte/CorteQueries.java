package web.controlevacinacao.repository.queries.corte;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.CorteFilter;
import web.controlevacinacao.model.Corte;

public interface CorteQueries {

	Page<Corte> pesquisar(CorteFilter filtro, Pageable pageable);
	
}
