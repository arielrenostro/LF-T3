package br.furb.linguagensformais.trabalho3.main;

import java.awt.EventQueue;

import br.furb.linguagensformais.trabalho3.factory.SwingViewFactory;
import br.furb.linguagensformais.trabalho3.view.LinguagemRegularView;

/**
 * @author ariel
 */
public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				LinguagemRegularView frame = SwingViewFactory.getView(LinguagemRegularView.class);
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
