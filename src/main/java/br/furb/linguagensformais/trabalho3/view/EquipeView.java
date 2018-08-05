package br.furb.linguagensformais.trabalho3.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author ariel
 */
public class EquipeView extends JDialog implements View {

	private static final long serialVersionUID = -9124845054210414160L;

	private final JPanel contentPanel = new JPanel();

	public EquipeView() {
		setTitle("Equipe");
		setBounds(100, 100, 450, 155);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(4, 1, 0, 0));

		JLabel lblFurbUniversidade = new JLabel("FURB - Universidade Regional de Blumenau");
		lblFurbUniversidade.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblFurbUniversidade);

		JLabel lblLinguagensFormais = new JLabel("Linguagens formais - Trabalho 3");
		lblLinguagensFormais.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblLinguagensFormais);

		JLabel label = new JLabel("2018/02");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(label);

		JLabel lblArielAdonaiSouza = new JLabel("Ariel Adonai Souza");
		lblArielAdonaiSouza.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblArielAdonaiSouza);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		okButton.addActionListener((action) -> setVisible(false));
	}

}
