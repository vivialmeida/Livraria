package livraria.service;

import livraria.model.Emprestimo;
import livraria.model.Livro;
import livraria.model.Usuario;
import livraria.service.exceptions.ObjectNotFoundException;
import livraria.service.interfaces.LivroService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LivroServiceImpl implements LivroService {

  @Override
  public void adicionarEmprestimo(Emprestimo emprestimo) {

      if (emprestimo == null)
        throw new ObjectNotFoundException("Informe um empréstimo!");

      Livro livro = emprestimo.getLivro();

      if (livro == null)
        throw new ObjectNotFoundException("Emprestimo sem um livro associado!");

      livro.setReservado(false);
      livro.setEmprestado(true);
      livro.adicionarEmprestimoAoHistorico(emprestimo);
    }



}
