package web.controlevacinacao.repository.queries.corte;

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
import web.controlevacinacao.filter.CorteFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Corte;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class CorteQueriesImpl implements CorteQueries {

	private static final Logger logger = LoggerFactory.getLogger(CorteQueriesImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Corte> pesquisar(CorteFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Corte> criteriaQuery = builder.createQuery(Corte.class);
		Root<Corte> v = criteriaQuery.from(Corte.class);
		TypedQuery<Corte> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();
		List<Predicate> predicateListTotal = new ArrayList<>();
		Predicate[] predArray;
		Predicate[] predArrayTotal;
		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(v.<Long>get("codigo"), filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getCliente())) {
			predicateList.add(builder.like(builder.lower(v.<String>get("cliente")),
					"%" + filtro.getCliente().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getBarbeador())) {
			predicateList.add(builder.like(builder.lower(v.<String>get("barbeador")),
					"%" + filtro.getBarbeador().toLowerCase() + "%"));
		}
		predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

		predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);
		criteriaQuery.select(v).where(predArray);
		PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);
		typedQuery = em.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		typedQuery.setHint("hibernate.query.passDistinctThrough", false);
		List<Corte> cortes = typedQuery.getResultList();
		logger.info("Calculando o total de registros que o filtro retornará.");
		CriteriaQuery<Long> criteriaQueryTotal = builder.createQuery(Long.class);
		Root<Corte> vTotal = criteriaQueryTotal.from(Corte.class);
		criteriaQueryTotal.select(builder.count(vTotal));
		if (filtro.getCodigo() != null) {
			predicateListTotal.add(builder.equal(vTotal.<Long>get("codigo"), filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getCliente())) {
			predicateListTotal.add(builder.like(builder.lower(vTotal.<String>get("cliente")),
					"%" + filtro.getCliente().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getBarbeador())) {
			predicateListTotal.add(builder.like(builder.lower(vTotal.<String>get("barbeador")),
					"%" + filtro.getBarbeador().toLowerCase() + "%"));
		}
		predicateListTotal.add(builder.equal(vTotal.<Status>get("status"), Status.ATIVO));
		
		predArrayTotal = new Predicate[predicateListTotal.size()];
		predicateListTotal.toArray(predArrayTotal);
		criteriaQueryTotal.where(predArrayTotal);
		TypedQuery<Long> typedQueryTotal = em.createQuery(criteriaQueryTotal);
		long totalCortes = typedQueryTotal.getSingleResult();
		logger.info("O filtro retornará {} registros.", totalCortes);
		Page<Corte> page = new PageImpl<>(cortes, pageable, totalCortes);
		return page;
	}

}
