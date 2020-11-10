package livraria.service.interfaces;

import com.jr.livraria.models.Emprestimo;
import com.jr.livraria.models.Livro;
import com.jr.livraria.models.Usuario;

import java.util.List;

public interface IUsuarioService {

    void adicionarEmprestimo(Emprestimo emprestimo);

    List<Emprestimo> consultarEmprestimos(Usuario usuario);

    Long quantidadeDeEmprestimoDe(Usuario usuario);

    Emprestimo buscaEmprestimoPara(Usuario usuario, Livro livro);

}
