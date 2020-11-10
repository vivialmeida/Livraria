package livraria.service;

import com.jr.livraria.models.Emprestimo;
import com.jr.livraria.models.Livro;
import com.jr.livraria.models.Usuario;
import com.jr.livraria.services.exceptions.ObjectNotFoundException;
import com.jr.livraria.services.interfaces.IUsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsuarioService implements IUsuarioService {

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

        return optionalEmprestimo.isPresent() ? optionalEmprestimo.get() : null;
    }






}
