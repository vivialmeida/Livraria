package livraria.service.interfaces;

import com.jr.livraria.models.Emprestimo;
import com.jr.livraria.models.Livro;
import com.jr.livraria.models.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface IEmprestimoService {

    Emprestimo emprestarPara(Usuario usuario, Livro livro);

    List<Emprestimo> consultarEmprestimosPorUsuario(Usuario usuario);

    Emprestimo devolverPara(Usuario usuario, Livro livro, LocalDate dataDeDevolucao);
}
