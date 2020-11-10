package livraria.service;

import livraria.model.Emprestimo;
import livraria.model.Livro;
import livraria.model.Usuario;
import livraria.service.exceptions.ObjectNotFoundException;
import livraria.service.interfaces.UsuarioService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioServiceImpl implements UsuarioService {

  @Override
  public void adicionarEmprestimo(Emprestimo emprestimo) {

    if (emprestimo == null)
      throw new ObjectNotFoundException("Informe um empréstimo!");

    Usuario usuario = emprestimo.getUsuario();

    if (usuario == null)
      throw new ObjectNotFoundException("Emprestimo sem um usuário associado!");

    usuario.adicionarEmprestimo(emprestimo);
  }

  public List<Emprestimo> consultarEmprestimos(Usuario usuario) {
    return usuario
        .getEmprestimos()
        .stream()
        .filter(emprestimo -> Objects.isNull(emprestimo.getDataDevolucao()))
        .collect(Collectors.toList());
  }

  public Long quantidadeDeEmprestimoDe(Usuario usuario) {
    return usuario
        .getEmprestimos()
        .stream()
        .filter(emprestimo -> Objects.isNull(emprestimo.getDataDevolucao()))
        .count();
  }

  public Emprestimo buscaEmprestimoPara(Usuario usuario, Livro livro) {
    Optional<Emprestimo> optionalEmprestimo = usuario
        .getEmprestimos()
        .stream()
        .filter(emprestimo -> emprestimo.getUsuario().equals(usuario) && emprestimo.getLivro().equals(livro))
        .findAny();

    return optionalEmprestimo.orElse(null);
  }
}
