package br.furb.linguagensformais.trabalho3.exception;

public class SimboloEspecialException extends ResultadoAnalisePalavraException {

	private static final long serialVersionUID = -8068127507793573994L;

	private static final String SIMBOLO_ESPECIAL = "Símbolo especial";

	public SimboloEspecialException() {
		super(SIMBOLO_ESPECIAL);
	}

}
