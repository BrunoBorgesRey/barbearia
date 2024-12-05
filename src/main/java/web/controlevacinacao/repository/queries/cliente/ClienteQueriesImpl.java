package web.controlevacinacao.repository.queries.cliente;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class ClienteQueriesImpl implements ClienteQueries {

	private static final Logger logger = LoggerFactory.getLogger(ClienteQueriesImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Cliente> pesquisar(ClienteFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteriaQuery = builder.createQuery(Cliente.class);
		Root<Cliente> v = criteriaQuery.from(Cliente.class);
		TypedQuery<Cliente> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();
		List<Predicate> predicateListTotal = new ArrayList<>();
		Predicate[] predArray;
		Predicate[] predArrayTotal;
		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(v.<Long>get("codigo"), filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getNome())) {
			predicateList.add(builder.like(builder.lower(v.<String>get("nome")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getDescricao())) {
			predicateList.add(builder.like(builder.lower(v.<String>get("descricao")),
					"%" + filtro.getDescricao().toLowerCase() + "%"));
		}
		predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

		predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);
		criteriaQuery.select(v).where(predArray);
		PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);
		typedQuery = em.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		typedQuery.setHint("hibernate.query.passDistinctThrough", false);
		List<Cliente> clientes = typedQuery.getResultList();
		logger.info("Calculando o total de registros que o filtro retornará.");
		CriteriaQuery<Long> criteriaQueryTotal = builder.createQuery(Long.class);
		Root<Cliente> vTotal = criteriaQueryTotal.from(Cliente.class);
		criteriaQueryTotal.select(builder.count(vTotal));
		if (filtro.getCodigo() != null) {
			predicateListTotal.add(builder.equal(vTotal.<Long>get("codigo"), filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getNome())) {
			predicateListTotal.add(builder.like(builder.lower(vTotal.<String>get("nome")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getDescricao())) {
			predicateListTotal.add(builder.like(builder.lower(vTotal.<String>get("descricao")),
					"%" + filtro.getDescricao().toLowerCase() + "%"));
		}
		predicateListTotal.add(builder.equal(vTotal.<Status>get("status"), Status.ATIVO));
		
		predArrayTotal = new Predicate[predicateListTotal.size()];
		predicateListTotal.toArray(predArrayTotal);
		criteriaQueryTotal.where(predArrayTotal);
		TypedQuery<Long> typedQueryTotal = em.createQuery(criteriaQueryTotal);
		long totalClientes = typedQueryTotal.getSingleResult();
		logger.info("O filtro retornará {} registros.", totalClientes);
		Page<Cliente> page = new PageImpl<>(clientes, pageable, totalClientes);
		return page;
	}

}
