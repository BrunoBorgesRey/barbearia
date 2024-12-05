package web.controlevacinacao.repository.queries.barbeador;

import java.time.LocalDate;
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
import web.controlevacinacao.filter.BarbeadorFilter;
import web.controlevacinacao.model.Barbeador;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class BarbeadorQueriesImpl implements BarbeadorQueries {

	private static final Logger logger = LoggerFactory.getLogger(BarbeadorQueriesImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Barbeador> pesquisar(BarbeadorFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Barbeador> criteriaQuery = builder.createQuery(Barbeador.class);
		Root<Barbeador> p = criteriaQuery.from(Barbeador.class);
		TypedQuery<Barbeador> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();
		List<Predicate> predicateListTotal = new ArrayList<>();
		Predicate[] predArray;
		Predicate[] predArrayTotal;
		if (filtro.getCodigo() != null) {
			predicateList.add(builder.equal(p.<Long>get("codigo"), filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getNome())) {
			predicateList.add(builder.like(builder.lower(p.<String>get("nome")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getCpf())) {
			predicateList.add(builder.like(builder.lower(p.<String>get("cpf")),
					"%" + filtro.getCpf().toLowerCase() + "%"));
		}
		if (filtro.getNascimentoDe() != null) {
			predicateList.add(builder.greaterThanOrEqualTo(p.<LocalDate>get("nascimento"),
					filtro.getNascimentoDe()));
		}
		predicateList.add(builder.equal(p.<Status>get("status"), Status.ATIVO));

		predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);
		criteriaQuery.select(p).where(predArray);
		PaginacaoUtil.prepararOrdem(p, criteriaQuery, builder, pageable);
		typedQuery = em.createQuery(criteriaQuery);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		typedQuery.setHint("hibernate.query.passDistinctThrough", false);
		List<Barbeador> barbeadors = typedQuery.getResultList();
		logger.info("Calculando o total de registros que o filtro retornará.");
		CriteriaQuery<Long> criteriaQueryTotal = builder.createQuery(Long.class);
		Root<Barbeador> pTotal = criteriaQueryTotal.from(Barbeador.class);
		criteriaQueryTotal.select(builder.count(pTotal));
		if (filtro.getCodigo() != null) {
			predicateListTotal.add(builder.equal(pTotal.<Long>get("codigo"), filtro.getCodigo()));
		}
		if (StringUtils.hasText(filtro.getNome())) {
			predicateListTotal.add(builder.like(builder.lower(pTotal.<String>get("nome")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getCpf())) {
			predicateListTotal.add(builder.like(builder.lower(pTotal.<String>get("cpf")),
					"%" + filtro.getCpf().toLowerCase() + "%"));
		}
		if (filtro.getNascimentoDe() != null) {
			predicateListTotal.add(builder.greaterThanOrEqualTo(pTotal.<LocalDate>get("nascimento"),
					filtro.getNascimentoDe()));
		}
		predicateList.add(builder.equal(pTotal.<Status>get("status"), Status.ATIVO));

		predArrayTotal = new Predicate[predicateListTotal.size()];
		predicateListTotal.toArray(predArrayTotal);
		criteriaQueryTotal.where(predArrayTotal);
		TypedQuery<Long> typedQueryTotal = em.createQuery(criteriaQueryTotal);
		long totalBarbeadors = typedQueryTotal.getSingleResult();
		logger.info("O filtro retornará {} registros.", totalBarbeadors);
		Page<Barbeador> page = new PageImpl<>(barbeadors, pageable, totalBarbeadors);
		return page;
	}

}
