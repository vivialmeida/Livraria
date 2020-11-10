package livraria.service;

import livraria.model.Emprestimo;
import livraria.model.Livro;
import livraria.model.Usuario;
import livraria.service.exceptions.EmprestimoValidationException;
import livraria.service.exceptions.LivroValidationException;
import livraria.service.exceptions.ObjectNotFoundException;
import livraria.service.interfaces.EmprestimoService;
import livraria.service.interfaces.LivroService;
import livraria.service.interfaces.UsuarioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EmprestimoServiceImpl implements EmprestimoService {

  private final int QTD_DIAS_PARA_DATA_PREVISTA = 7;
  private final int QTD_MAXIMA_DE_EMPRESTIMO = 2;
  private final BigDecimal VALOR_FIXO = new BigDecimal("5.00");
  private final BigDecimal TAXA_POR_ATRASO = new BigDecimal("0.40");
  private final BigDecimal TAXA_LIMITE = new BigDecimal("0.60");

  private final UsuarioService usuarioService;
  private final LivroService livroService;


  public EmprestimoServiceImpl(UsuarioService usuarioService, LivroService livroService) {
    this.usuarioService = usuarioService;
    this.livroService = livroService;
  }

  @Override
  public Emprestimo emprestarPara(Usuario usuario, Livro livro) {

    if (usuario == null)
      throw new ObjectNotFoundException("Para realizar o emprestimo informe um usuário!");

    if (livro == null)
      throw new ObjectNotFoundException("Para realizar o emprestimo informe um livro!");

    if (livro.isReservado())
      throw new EmprestimoValidationException("Livro Reservado!");

    if (livro.isEmprestado())
      throw new LivroValidationException("Livro Emprestado!") ;

    if (usuarioService.quantidadeDeEmprestimoDe(usuario) == QTD_MAXIMA_DE_EMPRESTIMO)
      throw new EmprestimoValidationException("Limite de emprestimos atingido! Qtd Maxima: "
          + QTD_MAXIMA_DE_EMPRESTIMO);

    Emprestimo emprestimo = Emprestimo.builder()
        .dataEmprestimo(LocalDate.now())
        .dataPrevista(LocalDate.now().plusDays(QTD_DIAS_PARA_DATA_PREVISTA))
        .valorDoAlugel(VALOR_FIXO.setScale(2))
        .usuario(usuario)
        .livro(livro)
        .build();

    usuarioService.adicionarEmprestimo(emprestimo);
    livroService.adicionarEmprestimo(emprestimo);

    return emprestimo;
  }

  @Override
  public List<Emprestimo> consultarEmprestimosPorUsuario(Usuario usuario) {
    return usuarioService.consultarEmprestimos(usuario);
  }

  @Override
  public Emprestimo devolverPara(Usuario usuario, Livro livro, LocalDate dataDeDevolucao) {

    if (usuario == null)
      throw new ObjectNotFoundException("Para realizar a devolucão do emprestimo informe um usuário!");

    if (livro == null)
      throw new ObjectNotFoundException("Para realizar a devolucão do emprestimo informe um livro!");

    if (!livro.isEmprestado())
      throw new EmprestimoValidationException("Este livro não está associado a este emprestimo!");

    Emprestimo emprestimo = usuarioService.buscaEmprestimoPara(usuario, livro);

    if (emprestimo == null)
      throw new EmprestimoValidationException("Nenhum emprestimo realizado para este usuário e para este livro!");

    livro.setEmprestado(false);
    emprestimo.setDataDevolucao(dataDeDevolucao);

    long qtdDeDiasDeAtraso = ChronoUnit.DAYS.between(emprestimo.getDataPrevista(), emprestimo.getDataDevolucao());

    if (qtdDeDiasDeAtraso > 0) {

      BigDecimal valorDaMulta = TAXA_POR_ATRASO.multiply(BigDecimal.valueOf(qtdDeDiasDeAtraso));
      BigDecimal limiteDoValorDaMultaA60PorCento = VALOR_FIXO.multiply(TAXA_LIMITE);

      if (valorDaMulta.compareTo(limiteDoValorDaMultaA60PorCento) == -1) {
        emprestimo.adicionaMulta(valorDaMulta);
      } else {
        emprestimo.adicionaMulta(limiteDoValorDaMultaA60PorCento);
      }

    }

    return emprestimo;
  }

}
