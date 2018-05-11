package pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/*
 * Essa Classe é utilizada para ter um controle maior sobre o que o usuário informa na janela de diálogo
 */

public class SmartField extends JTextField {
	
	private static final long serialVersionUID = 1L;
	private String caracteresPermitidos, placeholder, tipo;
	private double min, max;
	
	//Construtor simples
	public SmartField() {
		
		this("", "");
	}
	
	//Construtor com caracteres permitidos personalizados
	public SmartField(String caracteresPermitidos) {
		
		this("", caracteresPermitidos);
	}
	
	//Construtor com placeholder e caracteres permitidos personalizados
	public SmartField(String placeholder, String caracteresPermitidos) {
			
		//Construtor do JTextField
		super();
			
		//Definindo placeholder
		this.placeholder = placeholder;
			
		//Definindo conjunto de caracteres permitidos
		this.caracteresPermitidos = caracteresPermitidos;
		
		//Definindo tipo
		this.tipo = "STRING";
	}
	
	//Construtor simples para inputs numérios
	public SmartField(boolean decimal) {
		
		this(decimal, decimal ? Pane.DOUBLE_MIN : Pane.INT_MIN, decimal ? Pane.DOUBLE_MAX : Pane.INT_MAX);
	}
	
	//Construtor para inputs numéricos com limites
	public SmartField(boolean decimal, double min, double max) {
		
		//Construtor do JTextField
		super();
			
		//Definindo conjunto de caracteres permitidos e tipo
		if (decimal) {
			this.caracteresPermitidos = "1234567890-.";
			this.tipo = "DOUBLE";
		} else {
			this.caracteresPermitidos = "1234567890-";
			this.tipo = "INT";
		}
		
		//Definindo mínimo e máximo
		this.min = min;
		this.max = max;
	}

	//Sobrescrevendo método que trata os eventos acionados por teclas
	@Override
	protected void processKeyEvent(KeyEvent e) {
		
		atualizarListeners(e);
		
		char key = e.getKeyChar();
		
		//Definindo qual tipo de evento foi acionado
		switch (e.getID()) {
			
			case KeyEvent.KEY_TYPED: {	
				
				//Verificando qual tecla foi digitada
				if (key == KeyEvent.VK_BACK_SPACE) {
					
					//Limpa a última casa do texto caso necessário
					if (getText().length() > 0) {
						setText(getText().substring(0, getText().length() - 1));
					}
					
				//Adiciona o caractere ao TextField caso esteja na lista de caracteres permitidos
				//Caso a lista esteja vazia, o método entende que podem ser colocadas quaisquer caracteres
				} else {
				
					//Verificando tipo do TextField
					if (!tipo.equals("STRING")) {
						
						processarTiposNumericos(String.valueOf(e.getKeyChar()));
						
					} else {
						
						//Verificando se o caractere é permitido
						if ((caracteresPermitidos.contains(String.valueOf(key))) || (caracteresPermitidos.equals(""))) {
							
							setText(getText() + key);
						}
					}
				}
				
				//Verifica se deve desenhar o placeholder
				if (getText().isEmpty()) {
					
					desenharPlaceholder();
				}
			}
		}
	}
	
	//Métodos
	//Aparência
	public void desenharPlaceholder() {
		
		//Verifica se o placeholder existe
		if (this.placeholder == null) {
			return;
		}
		
		//Obtendo componente para desenhar na tela
		Graphics g = getGraphics();
		
		//Definindo fonte do graphics
		g.setFont(new Font("Arial", Font.ITALIC, 12));
		
		//Obtendo largura do edit
		int editWidth = (int) getBounds().getWidth();
		
		//Obtendo largura do texto
		FontMetrics metrics = g.getFontMetrics();
		int widthTexto = metrics.stringWidth(placeholder);
		
		//Calculando coordenadas para o desenho do placeholder
		int x = (editWidth / 2) - (widthTexto / 2), y = 18;
		
		//Desenha o placeholder
		g.setColor(Color.GRAY);
		g.drawString(placeholder, x, y);
	}

	public void sinalizarErro() {
		
		//Define a cor da borda como vermelho
		setBorder(BorderFactory.createLineBorder(Color.RED));
		
		//Define a cor do fundo como vermelho
		setBackground(Color.decode("#ffb3b3"));
		
		//Define a cor do texto como vermelho
		setForeground(Color.RED);
	}

	public void resetCores() {
		
		//Reseta cor da borda
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		//Reseta cor do fundo
		setBackground(Color.WHITE);
		
		//Reseta cor do texto
		setForeground(Color.BLACK);
	}

	//Funcionalidade
	private void atualizarListeners(KeyEvent e) {
		
		//Obtendo lista de keyListeners
		KeyListener[] listeners = getKeyListeners();
		
		for (KeyListener listener : listeners) {
			
			switch(e.getID()) {
			
				case KeyEvent.KEY_PRESSED: {
					
					listener.keyPressed(e);
					break;
				}
				case KeyEvent.KEY_TYPED: {
					
					listener.keyTyped(e);
					break;
				}
				
				case KeyEvent.KEY_RELEASED: {
					
					listener.keyReleased(e);
					break;
				}
			}
		}
	}

	private void processarTiposNumericos(String key) {
		
		//Verificando se o campo está preenchido com um 0
		if (getText().equals("0")) {
			
			//Limpa o campo
			setText("");
		}
		
		//Verificando se o caractere é um número
		if ("0123456789".contains(key)) {
			setText(getText() + key);
		} else {
			
			//Verificando se o símbolo já está no TextField
			if (!getText().contains(key)) {
				
				//Verificando se o símbolo é o sinal de menos
				if (key.charAt(0) == KeyEvent.VK_MINUS) {
					
					//Verificando o TextField está vazio
					if (getText().length() == 0) {
						
						setText(getText() + key);
					}
				} else {
					
					setText(getText() + key);
				}
			}
		}
		
		clampNumericos(key);
	}
	
	private void clampNumericos(String key) {
		
		//Tenta clampar o valor inserido
		try {
			
			if (tipo.equals("DOUBLE")) {
				clampDouble();
			} else {
				clampInt();
			}
			
		} catch (Exception ex) {
			
			//Define símbolos permitidos
			String simbolosPermitidos = tipo.equals("DOUBLE") ? "-." : "-";
			
			//Verifica se o caractere inserido não está nos símbolos permitidos
			if (!simbolosPermitidos.contains(key)) {
				setText("0");
			}
			
		}
	}
	
	private void clampInt() {
		
		int n = Integer.parseInt(getText());
		
		//Verifica se o número está abaixo do mínimo aceito
		if (n < min) {
			
			//Troca o conteúdo para o mínimo aceito
			setText(String.valueOf((int)min));
		}
		
		//Verifica se o número está acima do máximo aceito
		if (n > max) {
			
			//Troca o conteúdo para o máximo aceito
			setText(String.valueOf((int)max));
		}
	}

	private void clampDouble() {
		
		double n = Double.parseDouble(getText());
		
		//Verifica se o número está abaixo do mínimo aceito
		if (n < min) {
			
			//Troca o conteúdo para o mínimo aceito
			setText(String.valueOf(min));
		}
		
		//Verifica se o número está acima do máximo aceito
		if (n > max) {
			
			//Troca o conteúdo para o máximo aceito
			setText(String.valueOf(max));
		}
	}
	
	//Getters
	public double getDoubleText() {
		
		return Double.parseDouble(getText());
	}
	
	public int getIntText() {
		
		return Integer.parseInt(getText());
	}
}