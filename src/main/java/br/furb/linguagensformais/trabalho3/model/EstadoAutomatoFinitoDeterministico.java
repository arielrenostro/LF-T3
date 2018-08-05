package br.furb.linguagensformais.trabalho3.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ariel
 */
public class EstadoAutomatoFinitoDeterministico {

	private String codigo;
	private Map<Character, EstadoAutomatoFinitoDeterministico> conexoes = new HashMap<>();
	private boolean estadoFinal;

	public EstadoAutomatoFinitoDeterministico(String codigo) {
		this(codigo, false);
	}

	public EstadoAutomatoFinitoDeterministico(String codigo, boolean estadoFinal) {
		super();
		this.codigo = codigo;
		this.estadoFinal = estadoFinal;
	}

	public String getCodigo() {
		return codigo;
	}

	public void adicionarConexao(EstadoAutomatoFinitoDeterministico estado, Character character) {
		conexoes.put(character, estado);
	}

	public void removerConexaxo(Character character) {
		conexoes.remove(character);
	}

	public boolean isEstadoFinal() {
		return estadoFinal;
	}

	public Map<Character, EstadoAutomatoFinitoDeterministico> getConexoes() {
		return Collections.unmodifiableMap(conexoes);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] Codigo [" + this.codigo + "]";
	}
}
