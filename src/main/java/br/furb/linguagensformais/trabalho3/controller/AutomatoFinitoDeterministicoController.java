package br.furb.linguagensformais.trabalho3.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.furb.linguagensformais.trabalho3.exception.ControllerException;
import br.furb.linguagensformais.trabalho3.exception.PalavraNaoAceitaException;
import br.furb.linguagensformais.trabalho3.exception.ResultadoAnalisePalavraException;
import br.furb.linguagensformais.trabalho3.exception.SimboloEspecialException;
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
	private static final EstadoAutomatoFinitoDeterministico ESTADO_QESPECIAL = new EstadoAutomatoFinitoDeterministico("qespecial");

	private static final String ERRO_PALAVRA_INVALIDA = "erro: palavra inválida";
	private static final String ERRO_SIMBOLO_INVALIDO = "erro: símbolo(s) inválido(s)";
	private static final String MSG_PALAVRA_VAZIA = "Palavra vázia!";
	private static final String PALAVRA_VALIDA = "Palavra válida";
	private static final String PALAVRA_RESERVADA = "Palavra reservada";

	private static final String REGEX_QUEBRA_LINHA = "\\n";
	private static final int IDX_PRIMEIRO_CHAR_PALAVRA = 0;
	private static final int IDX_SEGUNDO_CHAR_PALAVRA = 1;

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
		EstadoAutomatoFinitoDeterministico estadoQ0 = new EstadoAutomatoFinitoDeterministico("q0");
		EstadoAutomatoFinitoDeterministico estadoQ6 = new EstadoAutomatoFinitoDeterministico("q6", true);
		EstadoAutomatoFinitoDeterministico estadoQ7 = new EstadoAutomatoFinitoDeterministico("q7");
		EstadoAutomatoFinitoDeterministico estadoQ1Q5 = new EstadoAutomatoFinitoDeterministico("(q1q5)", true);
		EstadoAutomatoFinitoDeterministico estadoQ2 = new EstadoAutomatoFinitoDeterministico("q2");
		EstadoAutomatoFinitoDeterministico estadoQ0Q1 = new EstadoAutomatoFinitoDeterministico("(q0q1)", true);
		EstadoAutomatoFinitoDeterministico estadoQ2Q6 = new EstadoAutomatoFinitoDeterministico("(q2q6)", true);
		EstadoAutomatoFinitoDeterministico estadoQ3 = new EstadoAutomatoFinitoDeterministico("q3", true);
		EstadoAutomatoFinitoDeterministico estadoQ4 = new EstadoAutomatoFinitoDeterministico("q4");

		estadoQ0.adicionarConexao(estadoQ1Q5, 'a');
		estadoQ0.adicionarConexao(estadoQ6, 'b');
		estadoQ0.adicionarConexao(estadoQ6, 'c');

		estadoQ6.adicionarConexao(estadoQ7, 'b');
		estadoQ6.adicionarConexao(estadoQ7, 'c');

		estadoQ7.adicionarConexao(estadoQ6, 'b');
		estadoQ7.adicionarConexao(estadoQ6, 'c');

		estadoQ1Q5.adicionarConexao(estadoQ0Q1, 'a');
		estadoQ1Q5.adicionarConexao(estadoQ2, 'b');
		estadoQ1Q5.adicionarConexao(estadoQ2, 'c');

		estadoQ2.adicionarConexao(estadoQ3, 'a');

		estadoQ0Q1.adicionarConexao(estadoQ1Q5, 'a');
		estadoQ0Q1.adicionarConexao(estadoQ2Q6, 'b');
		estadoQ0Q1.adicionarConexao(estadoQ2Q6, 'c');

		estadoQ2Q6.adicionarConexao(estadoQ3, 'a');
		estadoQ2Q6.adicionarConexao(estadoQ7, 'b');
		estadoQ2Q6.adicionarConexao(estadoQ7, 'c');

		estadoQ3.adicionarConexao(estadoQ4, 'a');
		estadoQ3.adicionarConexao(estadoQ2, 'b');
		estadoQ3.adicionarConexao(estadoQ2, 'c');

		estadoQ4.adicionarConexao(estadoQ3, 'a');

		AutomatoFinitoDeterministico automatoFinito = new AutomatoFinitoDeterministico();
		automatoFinito.setEstadoInicial(estadoQ0);
		automatoFinito.adicionarEstado(estadoQ0);
		automatoFinito.adicionarEstado(estadoQ6);
		automatoFinito.adicionarEstado(estadoQ7);
		automatoFinito.adicionarEstado(estadoQ1Q5);
		automatoFinito.adicionarEstado(estadoQ2);
		automatoFinito.adicionarEstado(estadoQ0Q1);
		automatoFinito.adicionarEstado(estadoQ2Q6);
		automatoFinito.adicionarEstado(estadoQ3);
		automatoFinito.adicionarEstado(estadoQ4);

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

		try {
			EstadoAutomatoFinitoDeterministico estadoCorrente = primeiraIteracao(palavra, resultadoAnalisePalavra, alfabeto, automatoFinito.getEstadoInicial());

			char caractere;
			for (int idxCaractere = IDX_SEGUNDO_CHAR_PALAVRA; idxCaractere < palavra.length(); idxCaractere++) {
				caractere = palavra.charAt(idxCaractere);
				resultadoAnalisePalavra.adicionarReconhecimento(estadoCorrente);

				validarCaractereForaAlfabeto(resultadoAnalisePalavra, alfabeto, caractere);
				estadoCorrente = getProximoEstado(caractere, estadoCorrente);
			}

			validarEstadoFinal(estadoCorrente);
			definirResultado(palavra, resultadoAnalisePalavra, estadoCorrente);
		} catch (ResultadoAnalisePalavraException e) {
			resultadoAnalisePalavra.setResultado(e.getMessage());

			if (e instanceof PalavraNaoAceitaException) {
				resultadoAnalisePalavra.adicionarReconhecimento(ESTADO_QERRO);
			}
		}
		return resultadoAnalisePalavra;
	}

	private void definirResultado(String palavra, ResultadoAnalisePalavra resultadoAnalisePalavra, EstadoAutomatoFinitoDeterministico estadoCorrente) {
		resultadoAnalisePalavra.setResultado(PALAVRAS_RESERVADAS.contains(palavra) ? PALAVRA_RESERVADA : PALAVRA_VALIDA);
		resultadoAnalisePalavra.adicionarReconhecimento(estadoCorrente);
	}

	private void validarEstadoFinal(EstadoAutomatoFinitoDeterministico estadoCorrente) throws PalavraNaoAceitaException {
		if (!estadoCorrente.isEstadoFinal()) {
			throw new PalavraNaoAceitaException(ERRO_PALAVRA_INVALIDA);
		}
	}

	private void validarCaractereForaAlfabeto(ResultadoAnalisePalavra resultadoAnalisePalavra, Set<Character> alfabeto, char caractere) throws PalavraNaoAceitaException {
		if (!alfabeto.contains(caractere)) {
			throw new PalavraNaoAceitaException(ERRO_PALAVRA_INVALIDA);
		}
	}

	private EstadoAutomatoFinitoDeterministico getProximoEstado(char caractere, EstadoAutomatoFinitoDeterministico estadoCorrente) throws PalavraNaoAceitaException {
		estadoCorrente = estadoCorrente.getConexoes().get(caractere);
		if (null == estadoCorrente) {
			throw new PalavraNaoAceitaException(ERRO_PALAVRA_INVALIDA);
		}
		return estadoCorrente;
	}

	private EstadoAutomatoFinitoDeterministico primeiraIteracao(String palavra, ResultadoAnalisePalavra resultadoAnalisePalavra, Set<Character> alfabeto, EstadoAutomatoFinitoDeterministico estadoCorrente) throws ResultadoAnalisePalavraException {
		resultadoAnalisePalavra.adicionarReconhecimento(estadoCorrente);

		char caractere = palavra.charAt(IDX_PRIMEIRO_CHAR_PALAVRA);
		validarCaractereEspecial(resultadoAnalisePalavra, caractere);
		validarCaractereForaAlfabetoPrimeiraIteracao(alfabeto, caractere);

		return getProximoEstado(caractere, estadoCorrente);
	}

	private void validarCaractereEspecial(ResultadoAnalisePalavra resultadoAnalisePalavra, char caractere) throws SimboloEspecialException {
		if (CARACTERES_ESPECIAIS.contains(caractere)) {
			resultadoAnalisePalavra.adicionarReconhecimento(ESTADO_QESPECIAL);
			throw new SimboloEspecialException();
		}
	}

	private void validarCaractereForaAlfabetoPrimeiraIteracao(Set<Character> alfabeto, char caractere) throws PalavraNaoAceitaException {
		if (!alfabeto.contains(caractere)) {
			throw new PalavraNaoAceitaException(ERRO_SIMBOLO_INVALIDO);
		}
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
