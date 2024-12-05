package web.controlevacinacao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "corte")
public class Corte implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="gerador2", sequenceName="corte_codigo_seq", allocationSize=1)
	@GeneratedValue(generator="gerador2", strategy=GenerationType.SEQUENCE)
	private Long codigo;
	@NotBlank(message = "O cliente do corte é obrigatório")
	private String cliente;
	@NotBlank(message = "O barbeador do corte é obrigatório")
	private String barbeador;
	@NotNull(message = "A data do corte é obrigatória")
	@Column(name = "data_corte")
	private LocalDate dataCorte;
	@NotNull(message = "A data do corte é obrigatória")
	@Column(name = "hora")
	private LocalTime hora;
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getBarbeador() {
		return barbeador;
	}

	public void setBarbeador(String barbeador) {
		this.barbeador = barbeador;
	}

	public LocalDate getDataCorte() {
		return dataCorte;
	}

	public void setDataCorte(LocalDate dataCorte) {
		this.dataCorte = dataCorte;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "codigo: " + codigo + "\\ncliente: " + cliente + "\\nbarbeador: " + barbeador + "\\ndataCorte: " + dataCorte
				+ "\\nhora: " + hora + "\\nstatus: " + status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Corte other = (Corte) obj;
		return Objects.equals(codigo, other.codigo);
	}

}
