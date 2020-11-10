package livraria.service;

import com.jr.livraria.models.Emprestimo;
import com.jr.livraria.models.Livro;
import com.jr.livraria.models.Usuario;
import com.jr.livraria.services.exceptions.EmprestimoValidationException;
import com.jr.livraria.services.interfaces.IEmprestimoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EmprestimoServiceTests {

    private EmprestimoService emprestimoService


    emprestimoService= new EmprestimoService( new UsuarioService(), new LivroService());

    private Usuario usuario01 = Usuario.builder()
            .nome("Viviane")
            .matricula("867")
            .emprestimos(new ArrayList<>())
            .build();

    private Usuario usuario02 = Usuario.builder()
            .nome("Teresa")
            .matricula("88894")
            .emprestimos(new ArrayList<>())
            .build();

    private Livro livro01 = Livro.builder()
            .titulo("Garota de texas")
            .autor("Jorge Smitch")
            .isEmprestado(false)
            .isReservado(false)
            .historico(new ArrayList<>())
            .build();

    private Livro livro02 = Livro.builder()
        .titulo("Alo Berenice")
        .autor("Jóao Castilho")
            .isEmprestado(false)
            .isReservado(true)
            .historico(new ArrayList<>())
            .build();

    private Livro livro03 = Livro.builder()
        .titulo("Casa verde ")
        .autor("Jorge Aragao")
            .isEmprestado(true)
            .isReservado(false)
            .historico(new ArrayList<>())
            .build();

    private Livro livro04 = Livro.builder()
            .autor("Pedrinho")
            .titulo("BBBB")
            .isEmprestado(false)
            .isReservado(false)
            .historico(new ArrayList<>())
            .build();


    @Test
    public void deveRealizarEmprestimoCasoOLivroNaoEstejaReservado() {
        assertDoesNotThrow(() -> emprestimoService.emprestarPara(usuario01, livro01));
    }

    @Test
    public void naoDeveFazerEmprestimoParaUmLivroQuePossuiUmaReserva() {
        assertThrows(EmprestimoValidationException.class, () -> emprestimoService.emprestarPara(usuario01, livro02));
    }

    @Test
    public void deveGarantirADataPrevistaCorretaAposLocacao() {

        Emprestimo emprestimo = emprestimoService.emprestarPara(usuario01, livro01);

        LocalDate dataPrevista = LocalDate.now().plusDays(7);
        LocalDate dataAtual = emprestimo.getDataPrevista();

        assertEquals(dataPrevista, dataAtual);

    }

    @Test
    public void usuarioDeveTerNenhumEmprestimo() {
        List<Emprestimo> emprestimos = emprestimoService.consultarEmprestimosPorUsuario(usuario01);
        assertEquals(true, emprestimos.isEmpty());
    }

    @Test
    @DisplayName("Usuario dever ter um emprestimos")
    public void usarioDeveTerUmEmprestimo() {

        emprestimoService.emprestarPara(usuario01, livro01);

        List<Emprestimo> emprestimos = emprestimoService
                .consultarEmprestimosPorUsuario(usuario01);

        assertEquals(1, emprestimos.size());

    }

    @Test
    @DisplayName("Usuario dever ter dois emprestimos")
    public void usuariodeveTerDoisEmprestimos() {
        emprestimoService.emprestarPara(usuario01, livro01);
        emprestimoService.emprestarPara(usuario01, livro04);

        List<Emprestimo> emprestimos = emprestimoService
                .consultarEmprestimosPorUsuario(usuario01);

        assertEquals(2, emprestimos.size());
    }

    @Test
    @DisplayName("Nao deve realizar o terceiro emprestimo")
    public void naoDeveRealizarOTerceiroEmprestimoParaOMesmoUsuario() {
        emprestimoService.emprestarPara(usuario01, livro01);
        emprestimoService.emprestarPara(usuario01, livro04);

        assertThrows(EmprestimoValidationException.class,
                () -> emprestimoService.emprestarPara(
                usuario01,
                Livro.builder().historico(new ArrayList<>()).build()
        ));
    }

    // ---------- Testes Para acao de devolver de emprestimo ------------

    @Test
    @DisplayName("Nao deve aplicar multa antes da Data Prevista")
    public void naoDeveAplicarMultaAntesDaDataPrevista() {

        emprestimoService.emprestarPara(usuario01, livro01);
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, LocalDate.now());

        assertEquals(new BigDecimal("5.00"), emprestimo.getValorDoAlugel());
    }

    @Test
    @DisplayName("Nao deve aplicar multa na data prevista")
    public void naoDeveAplicarMultaNaDataPrevista() {

        emprestimoService.emprestarPara(usuario01, livro01);

        LocalDate dataDeDevolucao = LocalDate.parse("2019-12-01");
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, dataDeDevolucao);

        assertEquals(new BigDecimal("5.0").setScale(2), emprestimo.getValorDoAlugel());
    }

    @Test
    @DisplayName("Deve aplicar a multa de 0.4 por 1 dia de atraso")
    public void deveAplicarMultaParaUmDiaAposAPrevista() {

        emprestimoService.emprestarPara(usuario01, livro01);

        LocalDate dataDeDevolucao = LocalDate.parse("2019-12-02");
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, dataDeDevolucao);

        assertEquals(new BigDecimal("5.4").setScale(2), emprestimo.getValorDoAlugel());

    }

    @Test
    @DisplayName("Deve aplicar uma multa de 60% para 30 dias de atraso")
    public void deveAplicarMultaDe60PorCentoPara30DiasDeAtraso() {

        emprestimoService.emprestarPara(usuario01, livro01);

        LocalDate dataDeDevolucao = LocalDate.parse("2019-12-31");
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, dataDeDevolucao);

        assertEquals(new BigDecimal("8.00").setScale(2), emprestimo.getValorDoAlugel());
    }


}
