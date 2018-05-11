package pane;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * Essa é a classe principal que possui todos os métodos utilizados para receber informações do usuário
 * Também é a classe da qual as classes InputPane e MultiInputPane herdam a janela
 */

public class Pane extends JDialog {

	/*
	 * INT_MIN = valor inteiro mínimo que pode ser inserido
	 * INT_MAX = valor inteiro máximo que pode ser inserido
	 * DOUBLE_MIN = valor double mínimo que pode ser inserido 
	 * DOUBLE_MAX = valor double máximo que pode ser inserido
	 * RESIZE_AS_NEEDED = informa à janela que ela deve ajustar sua largura de acordo com o texto 
	 * DEFAULT_SIZE = tamanho padrão da janela
	 */
	public static final int INT_MIN = Integer.MIN_VALUE, INT_MAX = Integer.MAX_VALUE, RESIZE_AS_NEEDED = 0,
			DEFAULT_SIZE = 350;
	public static final double DOUBLE_MIN = -Double.MAX_VALUE, DOUBLE_MAX = Double.MAX_VALUE;

	// Variáveis internas
	private int estado = 0;
	protected int lblWidth, lblHeight, width;

	// Construtor
	public Pane(String titulo, String texto, int width) {

		// Cria uma instância da variável de largura como string
		String widthS = width == 0 ? "" : Integer.toString(width - 100);

		// Configura a janela
		setTitle(titulo);
		setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Adiciona um listener que vai ver quando a janela for fechada
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {

				// Seta o estado -1, simbolizando para sair do programa
				setEstado(-1);
			}
		});

		// Formata o texto para os padrões html
		texto = texto.replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n",
				"<br/>");

		// Cria o label onde o texto aparecerá
		JLabel label = new JLabel();

		// Seta o texto do label
		label.setText("<html><p style=\"width:" + widthS + "; text-align:center;\">" + texto + "</p></html>");

		// Pega a largura e altura do label
		lblWidth = (int) label.getPreferredSize().getWidth();
		lblHeight = (int) label.getPreferredSize().getHeight();

		// Arruma a variável width de acordo com o width do label
		this.width = width == 0 ? lblWidth + 100 : width;

		// Arruma a posição do label
		label.setBounds(new Rectangle(50, 25, lblWidth, lblHeight));

		// Adiciona o label à janela
		add(label);

		// Arruma o tamanho da janela
		setBounds(0, 0, width, 100 + lblHeight);

		// Seta para a janela aparecer no meio da tela
		setLocationRelativeTo(null);

		// Deixa a janela visível
		setVisible(true);
		
		addKeyListener(new KeyAdap(this));
	}

	// Métodos
	/*
	 * O método input é usado para receber um valor do tipo string
	 */
	public static String input(String texto) {
		
		return InputPane.input("Entrada", texto);
	}
	
	public static String input(String texto, String caracteresPermitidos) {
		
		return InputPane.input("Entrada", texto, caracteresPermitidos);
	}
	
	public static String input(String texto, String titulo, String caracteresPermitidos, int width) {
		
		return InputPane.input(titulo, texto, caracteresPermitidos, width);
	}
	
	/*
	 * Os métodos inputInt e inputDouble são usados para receber valores do tipo int e double respectivamente
	 * A vantagem desse método, além da sintaxe simplificada, é que é possível definir um valor mínimo e máximo para o número informado pelo usuário
	 */
	public static int inputInt(String texto) {

		return (int) InputPane.input("Entrada Inteira", texto, false);
	}

	public static int inputInt(String texto, int min, int max) {
		
		return (int) InputPane.input("Entrada Inteira", texto, false, min, max);
	}
	
	public static int inputInt(String texto, String titulo, int width) {

		return (int) InputPane.input(titulo, texto, width, false);
	}

	public static int inputInt(String texto, String titulo, int width, int min, int max) {
		
		// Retorna o valor inserido na caixa de diálogo já convertido
		return  (int) InputPane.input(texto, titulo, width, false, min, max);
	}

	public static double inputDouble(String texto) {

		return (double) InputPane.input("Entrada Real", texto, true);
	}

	public static double inputDouble(String texto, double min, double max) {
		
		return (double) InputPane.input("Entrada Real", texto, true, min, max);
	}
	
	public static double inputDouble(String texto, String titulo, int width) {

		return (double) InputPane.input(titulo, texto, width, true);
	}

	public static double inputDouble(String texto, String titulo, int width, double min, double max) {

		//Retorna o valor inserido na caixa de diálogo já convertido
		return (double) InputPane.input(titulo, texto, width, true, min, max);	
	}

	/*
	 * O método inputMulti é usado para receber vários valores de diversos tipos de uma única vez
	 * 
	 * A matriz que o método deve receber deve seguir o seguinte padrão:
	 * 
	 * 		{{dica, tipo}}
	 * 
	 * exemplo:
	 * 
	 * 		{{"Nome", "String"}, {"Idade", "int"}, {"Altura", "double"}};
	 * 
	 * caso o tipo seja int ou double pode-se passar outros 2 parâmetros, sendo o mínimo e máximo respectivamente
	 * 
	 * exemplo:
	 * 
	 * 		{{"Insira um número", "int", 0, 10}};
	 * 
	 * caso o tipo seja string pode-se passar outro parâmetro, sendo os caracteres permitidos naquele campo de texto
	 * 
	 * exemplo:
	 * 
	 * 		{{"Escolha uma opção", "string", "abcde"}};
	 *		
	 */
	public static Object[] inputMulti(String texto, Object[][] inputs) {
		
		return inputMulti(texto, inputs, "Entrada Multipla", 250);
	}
	
	public static Object[] inputMulti(String texto, Object[][] inputs, String titulo, int width) {
		
		return MultiInputPane.input(titulo, texto, inputs, width);
	}
	
	protected static void esperaTerminar(Pane pane) {
		
		//Espera a janela terminar 
		while (pane.getEstado() == 0) {
			synchronized(pane) {}
		}
				
		//Verificando se é para terminar o programa
		if (pane.getEstado() == -1) {
			System.exit(0);
		}
	}
	
	// Getters e Setters
	protected void setEstado(int estado) {
		this.estado = estado;
	}

	protected int getEstado() {
		return this.estado;
	}
	
}
