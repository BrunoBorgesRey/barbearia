package web.controlevacinacao.filter;

import java.time.LocalDate;
import java.time.LocalTime;

public class CorteFilter {

    private Long codigo;
    private String cliente;
    private String barbeador;
    private LocalDate dataCorte;
    private LocalTime hora;
    private String status;
    private Boolean situacao;

    public Boolean getSituacao() {
        return situacao;
    }

    public void setSituacao(Boolean situacao) {
        this.situacao = situacao;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "codigo: " + codigo + "\\ncliente: " + cliente + "\\nbarbeador: " + barbeador + "\\ndataCorte: " + dataCorte
                + "\\nhora: " + hora + "\\nstatus: " + status;
    }

}
