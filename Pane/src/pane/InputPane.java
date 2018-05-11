package pane;

import java.awt.Rectangle;

import javax.swing.*;

/*
 * Essa classe é a usada para receber uma única informação do usuário
 */

public class InputPane extends Pane {

	private static final long serialVersionUID = 1L;
	
	private SmartField edit;

	//Construtor simples
	public InputPane(String titulo, String texto) {
		
		this(titulo, texto, "");
	}
	
	//Construtor com caracteres permitidos personalizados
	public InputPane(String titulo, String texto, String caracteresPermitidos) {
		
		this(titulo, texto, caracteresPermitidos, Pane.RESIZE_AS_NEEDED);
	}
	
	//Construtor com caracteres permitidos e largura personalizados
	public InputPane(String titulo, String texto, String caracteresPermitidos, int width) {
		
		// Construtor do pane
		super(titulo, texto, width);

		// Re-setando variável width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(caracteresPermitidos);
		
		terminarJanela();
	}
	
	//Construtor numérico simples
	public InputPane(String titulo, String texto, boolean decimal) {
		
		this(titulo, texto, Pane.RESIZE_AS_NEEDED, decimal);
	}
	
	//Construtor numérico com largura personalizada
	public InputPane(String titulo, String texto, int width, boolean decimal) {
		
		//Construtor do pane
		super(titulo, texto, width);
		
		//Re-setando variável width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(decimal);
		
		terminarJanela();
	}
	
	//Construtor numérico com limites personalizados
	public InputPane(String titulo, String texto, boolean decimal, double min, double max) {
		
		this(titulo, texto, Pane.RESIZE_AS_NEEDED, decimal, min, max);
	}
	
	//Construtor numérico com limites e largura personalizados
	public InputPane(String titulo, String texto, int width, boolean decimal, double min, double max ) {
		
		//Construtor do pane
		super(titulo, texto, width);
		
		//Re-setando variável width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(decimal, min, max);
		
		terminarJanela();
	}
	
	// Termina de construir a janela
	private void terminarJanela() {
		
		// Arruma a posição do texto dentro do edit
		edit.setHorizontalAlignment(SwingConstants.CENTER);

		// Arruma a posição do campo de entrada
		edit.setBounds(new Rectangle(50, 35 + lblHeight, width - 100, 25));

		// Adiciona um KeyAdapter para "ouvir" as teclas inseridas
		edit.addKeyListener(new KeyAdap(this));

		// Adiciona o campo de entrada à janela
		add(edit);

		// Adiciona um botão que finaliza a atividade à janela
		add(new FinishButton(this, width, 70 + lblHeight));

		// Arruma o tamanho da janela (145 seria a margem superior do label, o tamanho e margem do edit e do botão)
		setBounds(0, 0, width, 145 + lblHeight);

		// Seta para a janela aparecer no meio da tela
		setLocationRelativeTo(null);

		// Deixa a janela visível
		setVisible(true);

	}

	// Métodos
	public static String input(String titulo, String texto) {
		
		//Cria janela
		InputPane pane = new InputPane(titulo, texto);
		
		//Espera o usuário terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna valor inserido na janela
		return pane.getEdit().getText();
	}
	
	public static String input(String titulo, String texto, String caracteresPermitidos) {
	
		//Cria janela
		InputPane pane = new InputPane(titulo, texto, caracteresPermitidos);
		
		//Espera o usuário terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela
		return pane.getEdit().getText();
	}
	
	public static String input(String titulo, String texto, String caracteresPermitidos, int width) {
		
		//Cria a janela
		InputPane pane = new InputPane(titulo, texto, caracteresPermitidos, width);
		
		//Espera o usuário terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela
		return pane.getEdit().getText();
	}
	
	public static Object input(String titulo, String texto, boolean decimal) {
		
		//Cria a janela
		InputPane pane = new InputPane(titulo, texto, decimal);
		
		//Espera o usuário terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela já convertido
		return decimal ? pane.getEdit().getDoubleText() : pane.getEdit().getIntText();
	}
	
	public static Object input(String titulo, String texto, boolean decimal, double min, double max) {
		
		//Cria uma janela
		InputPane pane = new InputPane(titulo, texto, decimal, min, max);
		
		//Espera o usuário terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela já convertido
		return decimal ? pane.getEdit().getDoubleText() : pane.getEdit().getIntText();
	}
	
	public static Object input(String titulo, String texto, int width, boolean decimal) {
		
		//Cria a janela
		InputPane pane = new InputPane(titulo, texto, width, decimal);
		
		//Espera o usuário terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela já convertido
		return decimal ? pane.getEdit().getDoubleText() : pane.getEdit().getIntText();
	}
	
	public static Object input(String titulo, String texto, int width, boolean decimal, double min, double max) {
		
		//Cria uma janela
		InputPane pane = new InputPane(titulo, texto, width, decimal, min, max);
		
		//Espera o usuário terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela já convertido
		return decimal ? pane.getEdit().getDoubleText() : pane.getEdit().getIntText();
	}
	
	// Getters e Setters
	public SmartField getEdit() {
		return this.edit;
	}
}