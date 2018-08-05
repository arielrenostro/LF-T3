package br.furb.linguagensformais.trabalho3.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.furb.linguagensformais.trabalho3.exception.ControllerException;
import br.furb.linguagensformais.trabalho3.model.AutomatoFinitoDeterministico;
import br.furb.linguagensformais.trabalho3.model.EstadoAutomatoFinitoDeterministico;
import br.furb.linguagensformais.trabalho3.model.ResultadoAnalisePalavra;
import br.furb.linguagensformais.trabalho3.model.ResultadoExecucaoAutomatoFinito;

/**
 * @author ariel
 */
public class AutomatoFinitoDeterministicoController implements Controller {

	private static final Set<Character> CARACTERES_ESPECIAIS = new HashSet<>();
	private static final Set<String> PALAVRAS_RESERVADAS = new HashSet<>();
	private static final Character ESPACO = ' ';

	private static final EstadoAutomatoFinitoDeterministico ESTADO_QERRO = new EstadoAutomatoFinitoDeterministico("qerro");
	private static final String ERRO_PALAVRA_INVALIDA = "erro: palavra inválida";
	private static final String ERRO_SIMBOLO_INVALIDO = "erro: símbolo(s) inválido(s)";
	private static final String REGEX_QUEBRA_LINHA = "\\n";

	private static final String MSG_PALAVRA_VAZIA = "Palavra vázia!";
	private static final String SIMBOLO_ESPECIAL = "Símbolo especial";
	private static final String PALAVRA_VALIDA = "Palavra válida";
	private static final String PALAVRA_RESERVADA = "Palavra reservada";

	static {
		CARACTERES_ESPECIAIS.add(';');
		CARACTERES_ESPECIAIS.add('.');
		CARACTERES_ESPECIAIS.add(',');

		PALAVRAS_RESERVADAS.add("abaaa");
		PALAVRAS_RESERVADAS.add("acaaa");
		PALAVRAS_RESERVADAS.add("aaaba");
		PALAVRAS_RESERVADAS.add("aaaca");
	}

	private AutomatoFinitoDeterministico getAutomatoFinitoTrabalho3() {
		EstadoAutomatoFinitoDeterministico estadoQ0 = new EstadoAutomatoFinitoDeterministico("q0", true);
		EstadoAutomatoFinitoDeterministico estadoQ1 = new EstadoAutomatoFinitoDeterministico("q1");
		EstadoAutomatoFinitoDeterministico estadoQ2 = new EstadoAutomatoFinitoDeterministico("q2");
		EstadoAutomatoFinitoDeterministico estadoQ3 = new EstadoAutomatoFinitoDeterministico("q3");
		EstadoAutomatoFinitoDeterministico estadoQ4 = new EstadoAutomatoFinitoDeterministico("q4");
		EstadoAutomatoFinitoDeterministico estadoQ5 = new EstadoAutomatoFinitoDeterministico("q5");
		EstadoAutomatoFinitoDeterministico estadoQ6 = new EstadoAutomatoFinitoDeterministico("q6");
		EstadoAutomatoFinitoDeterministico estadoQ7 = new EstadoAutomatoFinitoDeterministico("q7");
		EstadoAutomatoFinitoDeterministico estadoQ8 = new EstadoAutomatoFinitoDeterministico("q8");

		estadoQ0.adicionarConexao(estadoQ5, 'a');
		estadoQ5.adicionarConexao(estadoQ0, 'a');

		AutomatoFinitoDeterministico automatoFinito = new AutomatoFinitoDeterministico();
		automatoFinito.setEstadoInicial(estadoQ0);
		automatoFinito.adicionarEstado(estadoQ0);
		automatoFinito.adicionarEstado(estadoQ1);
		automatoFinito.adicionarEstado(estadoQ2);
		automatoFinito.adicionarEstado(estadoQ3);
		automatoFinito.adicionarEstado(estadoQ4);
		automatoFinito.adicionarEstado(estadoQ5);
		automatoFinito.adicionarEstado(estadoQ6);
		automatoFinito.adicionarEstado(estadoQ7);
		automatoFinito.adicionarEstado(estadoQ8);

		return automatoFinito;
	}

	public ResultadoExecucaoAutomatoFinito executarAutomatoTrabalho3(String palavras) throws ControllerException {
		AutomatoFinitoDeterministico automatoFinito = getAutomatoFinitoTrabalho3();
		return executarAutomatoFinitoDeterministico(automatoFinito, palavras);
	}

	public ResultadoExecucaoAutomatoFinito executarAutomatoFinitoDeterministico(AutomatoFinitoDeterministico automatoFinito, String palavras) throws ControllerException {
		ResultadoExecucaoAutomatoFinito resultado = new ResultadoExecucaoAutomatoFinito();
		validarPalavras(palavras);

		List<String> linhas = getLinhas(palavras);

		String linha;
		List<String> palavrasList;
		ResultadoAnalisePalavra resultadoAnalisePalavra;
		for (int idxLinha = 0; idxLinha < linhas.size(); idxLinha++) {
			linha = linhas.get(idxLinha);
			palavrasList = getPalavras(linha);

			for (String palavra : palavrasList) {
				resultadoAnalisePalavra = analisarPalavra(palavra, automatoFinito, idxLinha);
				resultado.adicionarResultadoAnalisePalavra(resultadoAnalisePalavra);
			}
		}

		return resultado;
	}

	private void validarPalavras(String palavras) throws ControllerException {
		if (null == palavras || palavras.isEmpty()) {
			throw new ControllerException(MSG_PALAVRA_VAZIA);
		}
	}

	private ResultadoAnalisePalavra analisarPalavra(String palavra, AutomatoFinitoDeterministico automatoFinito, int idxLinha) {
		ResultadoAnalisePalavra resultadoAnalisePalavra = new ResultadoAnalisePalavra(palavra, idxLinha + 1);
		Set<Character> alfabeto = automatoFinito.getAlfabeto();

		EstadoAutomatoFinitoDeterministico estado = automatoFinito.getEstadoInicial();
		boolean primeiraIteracao = true;
		for (char caractere : palavra.toCharArray()) {
			resultadoAnalisePalavra.adicionarReconhecimento(estado);

			if (primeiraIteracao) {
				if (CARACTERES_ESPECIAIS.contains(caractere)) {
					resultadoAnalisePalavra.setResultado(SIMBOLO_ESPECIAL);
					resultadoAnalisePalavra.adicionarReconhecimento(new EstadoAutomatoFinitoDeterministico("FALTA"));
					return resultadoAnalisePalavra;
				}
			}

			if (!alfabeto.contains(caractere)) {
				resultadoAnalisePalavra.setResultado(primeiraIteracao ? ERRO_SIMBOLO_INVALIDO : ERRO_PALAVRA_INVALIDA);
				resultadoAnalisePalavra.adicionarReconhecimento(ESTADO_QERRO);
				return resultadoAnalisePalavra;
			}

			estado = estado.getConexoes().get(caractere);
			if (null == estado) {
				resultadoAnalisePalavra.setResultado(ERRO_PALAVRA_INVALIDA);
				resultadoAnalisePalavra.adicionarReconhecimento(ESTADO_QERRO);
				return resultadoAnalisePalavra;
			}

			if (primeiraIteracao) {
				primeiraIteracao = false;
			}
		}

		if (!estado.isEstadoFinal()) {
			resultadoAnalisePalavra.setResultado(ERRO_PALAVRA_INVALIDA);
			resultadoAnalisePalavra.adicionarReconhecimento(ESTADO_QERRO);
		} else {
			if (PALAVRAS_RESERVADAS.contains(palavra)) {
				resultadoAnalisePalavra.setResultado(PALAVRA_RESERVADA);
			} else {
				resultadoAnalisePalavra.setResultado(PALAVRA_VALIDA);
			}
		}

		return resultadoAnalisePalavra;
	}

	private List<String> getPalavras(String linha) {
		List<String> palavras = new ArrayList<>();
		StringBuilder palavraSB = new StringBuilder();

		for (char caractere : linha.toCharArray()) {
			if (CARACTERES_ESPECIAIS.contains(caractere) || ESPACO.equals(caractere)) {
				adicionarPalavraNaoVazia(palavras, palavraSB.toString());

				if (!ESPACO.equals(caractere)) {
					adicionarPalavraNaoVazia(palavras, caractere);
				}
				palavraSB = new StringBuilder();

			} else {
				palavraSB.append(caractere);
			}
		}

		adicionarPalavraNaoVazia(palavras, palavraSB.toString());
		return palavras;
	}

	private void adicionarPalavraNaoVazia(List<String> palavras, char carac) {
		StringBuilder sb = new StringBuilder();
		sb.append(carac);

		adicionarPalavraNaoVazia(palavras, sb.toString());
	}

	private void adicionarPalavraNaoVazia(List<String> palavras, String palavra) {
		if (!palavra.isEmpty()) {
			palavras.add(palavra);
		}
	}

	private List<String> getLinhas(String palavras) {
		// esta escrito no trabalho que n pode regex para os reconhecimentos de palavra...
		return Arrays.stream(palavras.split(REGEX_QUEBRA_LINHA)) //
				.map(String::trim) //
				.collect(Collectors.toList());
	}
}
