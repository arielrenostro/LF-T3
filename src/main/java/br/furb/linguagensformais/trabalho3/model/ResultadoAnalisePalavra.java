package br.furb.linguagensformais.trabalho3.model;

import java.util.ArrayList;
import java.util.List;

public class ResultadoAnalisePalavra {

	private String sequencia;
	private String resultado;
	private int linha;
	private List<EstadoAutomatoFinitoDeterministico> reconhecimento = new ArrayList<>();

	public ResultadoAnalisePalavra(String sequencia, int linha) {
		super();
		this.sequencia = sequencia;
		this.linha = linha;
	}

	public String getSequencia() {
		return sequencia;
	}

	public int getLinha() {
		return linha;
	}

	public void adicionarReconhecimento(EstadoAutomatoFinitoDeterministico estado) {
		reconhecimento.add(estado);
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getReconhecimento() {
		StringBuilder sb = new StringBuilder();

		for (EstadoAutomatoFinitoDeterministico estado : reconhecimento) {
			sb.append(estado.getCodigo());
			sb.append(", ");
		}

		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length() - 1);
		}

		return sb.toString();
	}

}
