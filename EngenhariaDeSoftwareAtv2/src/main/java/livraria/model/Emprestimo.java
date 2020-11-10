package livraria.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@EqualsAndHashCode(of = {"usario", "livro", "dataPrevista"})
public class Emprestimo {

    private LocalDate dataEmprestimo;
    private LocalDate dataPrevista;
    private LocalDate dataDevolucao;
    private BigDecimal valorDoAlugel;

    private Usuario usuario;
    private Livro livro;

    public void adicionaMulta(BigDecimal valorDaMulta) {
        this.valorDoAlugel = this.valorDoAlugel.add(valorDaMulta).setScale(2);

    }
}
