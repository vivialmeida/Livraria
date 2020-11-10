package livraria.service.interfaces;

import livraria.model.Emprestimo;
import livraria.model.Livro;
import livraria.model.Usuario;

import java.util.List;

public interface UsuarioService {

  void adicionarEmprestimo(Emprestimo emprestimo);

  List<Emprestimo> consultarEmprestimos(Usuario usuario);

  Long quantidadeDeEmprestimoDe(Usuario usuario);

  Emprestimo buscaEmprestimoPara(Usuario usuario, Livro livro);
}
