package pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
	private int maxLength;
	
	//Construtor simples
	public SmartField() {
		
		this(null, "", 0);
	}
	
	//Construtor com caracteres permitidos personalizados
	public SmartField(String caracteresPermitidos) {
		
		this(null, caracteresPermitidos, 0);
	}

	//Construtor com caracteres permitidos e tamanho máximo
	public SmartField(String caracteresPermitidos, int maxLength) {
		
		this(null, caracteresPermitidos, maxLength);
	}
	
	//Construtor com placeholder e caracteres permitidos personalizados
	public SmartField(String placeholder, String caracteresPermitidos) {
		
		this(placeholder, caracteresPermitidos, 0);
	}
	
	//Construtor com placeholder, caracteres permitidos e tamanho máximo personalizados
	public SmartField(String placeholder, String caracteresPermitidos, int maxLength) {
			
		//Construtor do JTextField
		super();
			
		//Definindo placeholder
		this.placeholder = placeholder;
			
		//Definindo conjunto de caracteres permitidos
		this.caracteresPermitidos = caracteresPermitidos;
		
		//Definindo tamanho máximo
		this.maxLength = maxLength;
		
		//Definindo tipo
		this.tipo = "STRING";
	}
	
	//Construtor simples para inputs numérios
	public SmartField(boolean decimal) {
		
		this(decimal, 0, 0);
	}
		
	//Construtor para inputs numéricos com limite pós vírgula
	public SmartField(boolean decimal, int maxLength) {
		
		this(decimal, null, maxLength);
	}
	
	//Construtor para inputs numéricos com placeholder
	public SmartField(boolean decimal, String placeholder) {
		
		this(decimal, placeholder, 0, 0, 0);
	}
	
	//Construtor para inputs numéricos com placeholder e limite pós vírgula
	public SmartField(boolean decimal, String placeholder, int maxLength) {
		
		this (decimal, placeholder, 0, 0, maxLength);
	}
	
	//Construtor para inputs numéricos com limites
	public SmartField(boolean decimal, double min, double max) {
		
		this(decimal, null, min, max, 0);
	}

	//Construtor para inputs numéricos com limites e limite pós vírgula
	public SmartField(boolean decimal, double min, double max, int maxLength) {
		
		this(decimal, null, min, max, maxLength);
	}
	
	//Construtor para inputs numéricos com limites e placeholder
	public SmartField(boolean decimal, String placeholder, double min, double max, int maxLength) {
		
		//Construtor do JTextField
		super();
					
		//Definindo tipo
		this.tipo = decimal ? "DOUBLE" : "INT";
				
		//Definindo mínimo e máximo
		this.min = min;
		this.max = max;
		
		//Definindo tamanho máximo pós vírgula
		this.maxLength = maxLength;
		
		//Definindo placeholder
		this.placeholder = placeholder;
		
		//Adicionando administrador de foco
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
				//Verificando se o tipo é numérico
				if (!tipo.equals("STRING")) {
					
					//Verificando se é decimal ou não
					if (decimal) {
						
						//Verifica se o número inserido é menor que o mínimo
						if (getDouble() < min) {
							
							//Seta o texto como o mínimo
							setText(String.valueOf(min));
							
						//Verifica se o número inserido é maior que o mínimo
						} else if (getDouble() > max) {
							
							setText(String.valueOf(max));
						}
					} else {
						
						//Verifica se o número inserido é menor que o mínimo
						if (getInt() < min) {
							
							//Seta o texto como o mínimo
							setText(String.valueOf(min));
							
						//Verifica se o número inserido é maior que o mínimo
						} else if (getInt() > max) {
							
							setText(String.valueOf(max));
						}
					}
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//Sobrescrevendo método que trata os eventos acionados por teclas
	@Override
	protected void processKeyEvent(KeyEvent e) {
		
		atualizarListeners(e);
		
		char key = e.getKeyChar();
		
		if (e.getID() == KeyEvent.KEY_TYPED ) {
			
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
						
						//Verificando se o length máximo ainda não foi atingido ou se é livre
						if ((getText().length() < maxLength) || (maxLength == 0)) {
							
							setText(getText() + key);
						}
					}
				}
			}
			
			//Verifica se deve desenhar o placeholder
			if (getText().isEmpty()) {
				
				desenharPlaceholder();
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
		
		//Verificando se o caractere é um número
		if ("0123456789".contains(key)) {
			
			//Verifica se o texto é 0
			if (getText().equals("0")) {
			
				setText("");
			}	
			
			//Verifica de qual tipo é o SmartField
			if (tipo.equals("DOUBLE")) {
				
				verificarLengthPosVirgula(key);
			} else {
				
				setText(getText() + key);
				
			}
		} else {
			
			processarSimbolos(key);
		}
		
		clampNumericos(key);
	}
	
	private void verificarLengthPosVirgula(String key) {
		
		int index = getText().indexOf(".");
		
		//Verifica se já existe um . no texto e se existe um limite
		//O objeto entende que 0 é sem limite
		if ((index > -1) && (maxLength > 0)) {
			
			//Verifica se o tamanho máximo ainda não foi atingido
			if ((getText().length() - (index + 1)) < maxLength) {
				setText(getText() + key);
			}
		} else {
			
			setText(getText() + key);
		}
	}
	
	private void processarSimbolos(String key) {
		
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
	
	private void clampNumericos(String key) {
		
		//Tenta clampar o valor inserido
		try {
			
			if (tipo.equals("DOUBLE")) {
				clampDouble();
			} else {
				clampInt();
			}
			
		} catch (Exception ex) {
			
			//Verificando se a última letra digitada é um ponto
			if (key.equals(".")) {
				return;
			}
			
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
		
		//Verifica se precisa ajustar
		if (min != max) {
			
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
	}

	private void clampDouble() {
		
		double n = Double.parseDouble(getText());
		
		//Verifica se precisa ajustar
		if (min != max) {
			
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
	}
	
	//Getters e Setters
	public double getDouble() {
		
		return Double.parseDouble(getText());
	}
	
	public int getInt() {
		
		return Integer.parseInt(getText());
	}
}