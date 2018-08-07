package br.furb.linguagensformais.trabalho3.view;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.furb.linguagensformais.trabalho3.controller.AutomatoFinitoDeterministicoController;
import br.furb.linguagensformais.trabalho3.exception.ControllerException;
import br.furb.linguagensformais.trabalho3.factory.ControllerFactory;
import br.furb.linguagensformais.trabalho3.factory.SwingViewFactory;
import br.furb.linguagensformais.trabalho3.model.ResultadoAnalisePalavra;
import br.furb.linguagensformais.trabalho3.model.ResultadoExecucaoAutomatoFinito;
import br.furb.linguagensformais.trabalho3.view.components.TextLineNumber;

/**
 * @author ariel
 */
public class LinguagemRegularView extends JFrame implements View {

	private static final long serialVersionUID = -4983299331073437995L;

	private static final int IDX_COLUNA_LINHA = 0;
	private static final int IDX_COLUNA_RESULTADO = 1;

	private static final int WIDTH_COLUNA_LINHA = 70;
	private static final int WIDTH_COLUNA_RESULTADO = 180;
	private static final int WIDTH_MIN = 200;
	private static final int MARGEM = 10;

	private final JPanel contentPane;
	private final JTextPane txtPaneCodigo = new JTextPane();
	private final JPanel pnlBotoes = new JPanel();

	private final JButton btAnalisar = new JButton("Analisar");
	private final JButton btLimpar = new JButton("Limpar");
	private final JButton btEquipe = new JButton("Equipe");
	private final JTable tblResultados = new JTable();

	public LinguagemRegularView() throws IOException {
		setTitle("Reconhecedor de linguagem regular");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 480);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		txtPaneCodigo.setToolTipText("Insira as palavras aqui");
		txtPaneCodigo.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));

		btAnalisar.setIcon(scaleImage(20, 20, "/icons/play.png"));
		btLimpar.setIcon(scaleImage(23, 23, "/icons/clean.png"));
		btEquipe.setIcon(scaleImage(20, 20, "/icons/team.png"));

		pnlBotoes.setBounds(0, 200, 800, 45);
		pnlBotoes.setLayout(new GridLayout(1, 3, 0, 0));
		pnlBotoes.add(btAnalisar);
		pnlBotoes.add(btLimpar);
		pnlBotoes.add(btEquipe);

		tblResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblResultados.setModel(getTableModel());

		corrigirLayout();
		adicionarEventosBotoes();
	}

	private void adicionarEventosBotoes() {
		adicionarEventoBotaoAnalisar();
		adicionarEventoBotaoLimpar();
		adicionarEventoBotaoEquipe();
	}

	private void adicionarEventoBotaoAnalisar() {
		btAnalisar.addActionListener((action) -> {
			try {
				String palavras = txtPaneCodigo.getText();
				AutomatoFinitoDeterministicoController automatoFinitoDeterministicoController = ControllerFactory.getController(AutomatoFinitoDeterministicoController.class);
				ResultadoExecucaoAutomatoFinito resultadoExecucaoAutomato = automatoFinitoDeterministicoController.executarAutomatoTrabalho3(palavras);
				atualizarResultadoExcecucaoLinguagemRegular(resultadoExecucaoAutomato);

				mostrarInfoDialog("Analise concluída!");
			} catch (ControllerException e) {
				mostrarErroDialog(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				mostrarErroDialog("Erro não esperado [" + e.getMessage() + "]");
			}
		});
	}

	private void atualizarResultadoExcecucaoLinguagemRegular(ResultadoExecucaoAutomatoFinito resultadoExecucaoAutomato) {
		popularDadosTabela(resultadoExecucaoAutomato);
		redimencionarTamanhoColunasTabela();
	}

	private void redimencionarTamanhoColunasTabela() {
		TableColumn columnLinha = tblResultados.getColumnModel().getColumn(IDX_COLUNA_LINHA);
		TableColumn columnResultado = tblResultados.getColumnModel().getColumn(IDX_COLUNA_RESULTADO);

		setTamanhoColuna(columnLinha, WIDTH_COLUNA_LINHA);
		setTamanhoColuna(columnResultado, WIDTH_COLUNA_RESULTADO);

		for (int idxColuna = IDX_COLUNA_RESULTADO + 1; idxColuna < tblResultados.getColumnCount(); idxColuna++) {
			TableColumn column = tblResultados.getColumnModel().getColumn(idxColuna);
			int menorLargura = WIDTH_MIN;

			for (int idxLinha = 0; idxLinha < tblResultados.getRowCount(); idxLinha++) {
				TableCellRenderer cellRenderer = tblResultados.getCellRenderer(idxLinha, idxColuna);
				Component c = tblResultados.prepareRenderer(cellRenderer, idxLinha, idxColuna);
				int width = c.getPreferredSize().width + tblResultados.getIntercellSpacing().width;
				menorLargura = Math.max(menorLargura, width);
			}

			menorLargura += MARGEM;
			setTamanhoColuna(column, menorLargura);
		}
	}

	private void setTamanhoColuna(TableColumn column, int largura) {
		column.setMaxWidth(largura);
		column.setMinWidth(largura);
		column.setPreferredWidth(largura);
	}

	private void popularDadosTabela(ResultadoExecucaoAutomatoFinito resultadoExecucaoAutomato) {
		List<ResultadoAnalisePalavra> resultadosAnalisePalavra = resultadoExecucaoAutomato.getResultadosAnalisePalavra();
		DefaultTableModel tableModel = getTableModel();

		String linhaStr;
		String resultadoStr;
		String sequenciaStr;
		String reconhecimentoStr;
		for (ResultadoAnalisePalavra resultadoAnalisePalavra : resultadosAnalisePalavra) {
			linhaStr = String.valueOf(resultadoAnalisePalavra.getLinha());
			resultadoStr = resultadoAnalisePalavra.getResultado();
			sequenciaStr = resultadoAnalisePalavra.getSequencia();
			reconhecimentoStr = resultadoAnalisePalavra.getReconhecimento();

			tableModel.addRow(new Object[] { linhaStr, resultadoStr, sequenciaStr, reconhecimentoStr });
		}

		tblResultados.setModel(tableModel);
	}

	private void adicionarEventoBotaoEquipe() {
		btEquipe.addActionListener((action) -> SwingViewFactory.getView(EquipeView.class).setVisible(true));
	}

	private void adicionarEventoBotaoLimpar() {
		btLimpar.addActionListener((action) -> {
			txtPaneCodigo.setText("");
			tblResultados.setModel(getTableModel());
			mostrarInfoDialog("Limpo!");
		});
	}

	public ImageIcon scaleImage(int width, int height, String filename) {
		BufferedImage bi = null;
		try {
			ImageIcon ii = new ImageIcon(ImageIO.read(getClass().getResource(filename)));
			bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
			Graphics2D g2d = bi.createGraphics();
			g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
			g2d.drawImage(ii.getImage(), 0, 0, width, height, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new ImageIcon(bi);
	}

	private DefaultTableModel getTableModel() {
		String[] nomeColunas = new String[] { "Linha", "Resultado", "Sequência", "Reconhecimento" };
		Object[][] dados = new Object[][] {};
		return new DefaultTableModel(dados, nomeColunas);
	}

	private void corrigirLayout() {
		contentPane.setLayout(null);

		JScrollPane scrollCodigo = new JScrollPane(txtPaneCodigo);
		scrollCodigo.setBounds(0, 0, 800, 200);
		scrollCodigo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollCodigo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollCodigo.setRowHeaderView(new TextLineNumber(txtPaneCodigo));

		JScrollPane scrollTblResultados = new JScrollPane(tblResultados);
		scrollTblResultados.setBounds(0, 245, 800, 200);
		scrollTblResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollTblResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		tblResultados.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		contentPane.add(scrollCodigo);
		contentPane.add(pnlBotoes);
		contentPane.add(scrollTblResultados);

		redimencionarTamanhoColunasTabela();
	}

	private void mostrarInfoDialog(String info) {
		JOptionPane.showMessageDialog(this, info, "Informação", JOptionPane.INFORMATION_MESSAGE);
	}

	private void mostrarErroDialog(String erro) {
		JOptionPane.showMessageDialog(this, erro, "Erro!", JOptionPane.ERROR_MESSAGE);
	}
}
