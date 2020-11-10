package livraria.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Builder
@Data
@ToString(of = {"nome", "matricula"})
@EqualsAndHashCode(of = {"nome", "matricula"})
public class Usuario {

    private String nome;
    private String matricula;

    private List<Emprestimo> emprestimos;


    public void adicionarEmprestimo(Emprestimo emprestimo) {
        this.emprestimos.add(emprestimo);
    }

    public Integer getQuantidadeDeEmprestimos() {
        return this.emprestimos.size();
    }

}
