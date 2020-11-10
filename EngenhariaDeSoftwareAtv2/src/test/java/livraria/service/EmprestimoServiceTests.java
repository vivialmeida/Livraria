package livraria.service;


import livraria.model.Emprestimo;
import livraria.model.Livro;
import livraria.model.Usuario;
import livraria.service.exceptions.EmprestimoValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class EmprestimoServiceTests {

    private EmprestimoServiceImpl emprestimoService=  new EmprestimoServiceImpl( new UsuarioServiceImpl(), new LivroServiceImpl());

    private Usuario usuario01 = Usuario.builder()
        .nome("Viviane")
        .matricula("867")
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
        .autor("Paulo Ferreira")
        .titulo("Bom tempo")
        .isEmprestado(false)
        .isReservado(false)
        .historico(new ArrayList<>())
        .build();



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
    public void usarioDeveTerUmEmprestimo() {

        usuario01.getEmprestimos();
        emprestimoService.emprestarPara(usuario01, livro01);

        List<Emprestimo> emprestimos = emprestimoService
            .consultarEmprestimosPorUsuario(usuario01);

        assertEquals(1, emprestimos.size());

    }

    @Test
    public void usuariodeveTerDoisEmprestimos() {
        emprestimoService.emprestarPara(usuario01, livro01);
        emprestimoService.emprestarPara(usuario01, livro03);

        List<Emprestimo> emprestimos = emprestimoService
            .consultarEmprestimosPorUsuario(usuario01);

        assertEquals(2, emprestimos.size());
    }

    @Test
    public void naoDeveRealizarOTerceiroEmprestimoParaOMesmoUsuario() {
        emprestimoService.emprestarPara(usuario01, livro01);
        emprestimoService.emprestarPara(usuario01, livro03);

        assertThrows(EmprestimoValidationException.class,
            () -> emprestimoService.emprestarPara(
                usuario01,
                Livro.builder().historico(new ArrayList<>()).build()
            ));
    }

    @Test
    public void naoDeveAplicarMultaAntesDaDataPrevista() {

        emprestimoService.emprestarPara(usuario01, livro01);
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, LocalDate.now());

        assertEquals(new BigDecimal("5.00"), emprestimo.getValorDoAlugel());
    }

    @Test
    public void naoDeveAplicarMultaNaDataPrevista() {

        emprestimoService.emprestarPara(usuario01, livro01);

        LocalDate dataDeDevolucao = LocalDate.parse("2019-12-01");
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, dataDeDevolucao);

        assertEquals(new BigDecimal("5.0").setScale(2), emprestimo.getValorDoAlugel());
    }

    @Test
    public void deveAplicarMultaParaUmDiaAposAPrevista() {

        emprestimoService.emprestarPara(usuario01, livro01);

        LocalDate dataDeDevolucao = LocalDate.now().plusDays(8);
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, dataDeDevolucao);

        assertEquals(new BigDecimal("5.4").setScale(2), emprestimo.getValorDoAlugel());

    }

    @Test
    public void deveAplicarMultaDe60PorCentoPara30DiasDeAtraso() {

        emprestimoService.emprestarPara(usuario01, livro01);

        LocalDate dataDeDevolucao = LocalDate.now() .plusDays(60);
        Emprestimo emprestimo = emprestimoService.devolverPara(usuario01, livro01, dataDeDevolucao);

        assertEquals(new BigDecimal("8.00").setScale(2), emprestimo.getValorDoAlugel());
    }


}
