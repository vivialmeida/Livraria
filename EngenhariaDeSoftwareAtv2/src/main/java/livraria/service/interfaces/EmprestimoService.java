package livraria.service.interfaces;

import livraria.model.Emprestimo;
import livraria.model.Livro;
import livraria.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoService {

  Emprestimo emprestarPara(Usuario usuario, Livro livro);

  List<Emprestimo> consultarEmprestimosPorUsuario(Usuario usuario);

  Emprestimo devolverPara(Usuario usuario, Livro livro, LocalDate dataDeDevolucao);
}
