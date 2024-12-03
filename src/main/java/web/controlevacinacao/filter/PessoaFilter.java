package web.controlevacinacao.filter;

import java.time.LocalDate;

public class PessoaFilter {

    private Long codigo;
    private String nome;
    private String cpf;
    private LocalDate nascimentoDe;
    private LocalDate nascimentoAte;
    private String profissao;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getNascimentoDe() {
        return nascimentoDe;
    }

    public void setNascimentoDe(LocalDate nascimentoDe) {
        this.nascimentoDe = nascimentoDe;
    }

    public LocalDate getNascimentoAte() {
        return nascimentoAte;
    }

    public void setNascimentoAte(LocalDate nascimentoAte) {
        this.nascimentoAte = nascimentoAte;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    @Override
    public String toString() {
        return "codigo: " + codigo + "\\nnome: " + nome + "\\ncpf: " + cpf + "\\nnascimentoDe: " + nascimentoDe
                + "\\nnascimentoAte: " + nascimentoAte + "\\nprofissao: " + profissao;
    }

}
