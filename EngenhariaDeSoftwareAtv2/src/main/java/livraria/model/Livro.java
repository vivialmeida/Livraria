package livraria.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Builder
@Data
@ToString(of = {"autor, titulo", "isEmprestado", "isReservado"})
public class Livro {

    private String autor;
    private String titulo;
    private boolean isEmprestado;
    private boolean isReservado;

    private List<Emprestimo> historico;

    public void adicionarEmprestimoAoHistorico(Emprestimo emprestimo) {
        this.historico.add(emprestimo);
    }
}
