package br.furb.linguagensformais.trabalho3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ariel
 */
public class ResultadoExecucaoAutomatoFinito {

	private List<ResultadoAnalisePalavra> resultadosAnalisePalavra = new ArrayList<>();

	public ResultadoExecucaoAutomatoFinito() {
		super();
	}

	public void adicionarResultadoAnalisePalavra(ResultadoAnalisePalavra resultadoAnalisePalavra) {
		resultadosAnalisePalavra.add(resultadoAnalisePalavra);
	}

	public List<ResultadoAnalisePalavra> getResultadosAnalisePalavra() {
		return Collections.unmodifiableList(resultadosAnalisePalavra);
	}
}
