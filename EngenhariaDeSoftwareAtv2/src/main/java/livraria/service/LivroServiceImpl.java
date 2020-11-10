package livraria.service;

import com.jr.livraria.models.Emprestimo;
import com.jr.livraria.models.Livro;
import com.jr.livraria.services.exceptions.ObjectNotFoundException;
import com.jr.livraria.services.interfaces.ILivroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LivroService implements ILivroService {

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
