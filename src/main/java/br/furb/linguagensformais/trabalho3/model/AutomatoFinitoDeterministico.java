package br.furb.linguagensformais.trabalho3.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ariel
 */
public class AutomatoFinitoDeterministico {

	private Set<EstadoAutomatoFinitoDeterministico> estados = new HashSet<>();
	private Set<Character> alfabeto = new HashSet<>();
	private EstadoAutomatoFinitoDeterministico estadoInicial;

	public void adicionarEstado(EstadoAutomatoFinitoDeterministico estado) {
		estados.add(estado);
		atualizarAlfabeto();
	}

	public void removerEstado(EstadoAutomatoFinitoDeterministico estado) {
		estados.remove(estado);
		atualizarAlfabeto();
	}

	public EstadoAutomatoFinitoDeterministico getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(EstadoAutomatoFinitoDeterministico estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public Set<Character> getAlfabeto() {
		return Collections.unmodifiableSet(alfabeto);
	}

	private void atualizarAlfabeto() {
		// Sim, nada performatico... :(
		alfabeto.clear();
		estados.stream() //
				.flatMap(estado -> estado.getConexoes().keySet().stream()) //
				.forEach(chaveConexao -> alfabeto.add(chaveConexao));
	}
}
