package pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/*
 * Essa Classe é utilizada para ter um controle maior sobre o que o usuário informa na janela de diálogo
 */

public class SmartField extends JTextField {
	
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
		
		//Definindo qual tipo de evento foi acionado
		switch (e.getID()) {
			
			case KeyEvent.KEY_TYPED: {	
				
				//Obtendo lista de key listeners
				KeyListener[] listeners = getKeyListeners();
				
				//Passando evento para os key listeners
				for (KeyListener listener : listeners) {
					listener.keyTyped(e);
				}
				
				//Verificando qual tecla foi digitada
				if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					
					//Limpa a última casa do texto caso necessário
					if (getText().length() > 0) {
						setText(getText().substring(0, getText().length() - 1));
					}
					
				//Adiciona o caractere ao TextField caso esteja na lista de caracteres permitidos
				//Caso a lista esteja vazia, o método entende que podem ser colocadas quaisquer caracteres
				} else if (caracteresPermitidos.contains(String.valueOf(e.getKeyChar())) || (caracteresPermitidos.isEmpty())) {
				
					//Verificando se o tipo do TextField é numérico
					if (!tipo.equals("STRING")) {
						
						//Verificando se o campo está preenchido com um 0
						if (getText().equals("0")) {
							
							//Limpa o campo
							setText("");
						}
						
						//Verificando se o caractere é um número
						if ("0123456789".contains(String.valueOf(e.getKeyChar()))) {
							setText(getText() + e.getKeyChar());
						} else {
							
							//Verificando se o símbolo já está no TextField
							if (!getText().contains(String.valueOf(e.getKeyChar()))) {
								
								//Verificando se o símbolo é o sinal de menos
								if (e.getKeyChar() == KeyEvent.VK_MINUS) {
									
									//Verificando o TextField está vazio
									if (getText().length() == 0) {
										
										setText(getText() + e.getKeyChar());
									}
								} else {
									
									setText(getText() + e.getKeyChar());
								}
							}
						}
					} else {
						setText(getText() + e.getKeyChar());
					}
					
					//Verificando se a caixa de diálogo deve apenas receber valores do tipo inteiro ou real
					switch (tipo.toUpperCase()) {
						
						case "INT": {
							
							//Verificando se é possível converter o valor no TextField para integer
							try {
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
							} catch(Exception ex) {
								
								//Verifica se o caractere é -
								if (e.getKeyChar() != KeyEvent.VK_MINUS) {
									
									//Caso não seja possível converter o número o campo é preenchido com 0
									setText("0");
								}						
							}
							break;
						}
						case "DOUBLE": {
							
							//Verificando se é possível converter o valor no TextField para double
							try {
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
							} catch(Exception ex) {
								
								//Verifica se o caractere é -
								if (e.getKeyChar() != KeyEvent.VK_MINUS) {
									
									//Caso não seja possível converter o número o campo é preenchido com 0
									setText("0");
								}
							}
							
							break;
						}
					}
				}
				
				//Verifica se deve desenhar o placeholder
				if (getText().isEmpty()) {
					
					desenharPlaceholder();
				}
			}
			
			case KeyEvent.KEY_PRESSED: {
				
				//Obtendo lista de key listeners
				KeyListener[] listeners = getKeyListeners();
				
				//Passando evento para os key listeners
				for (KeyListener listener : listeners) {
					listener.keyPressed(e);
				}
			}
			
			case KeyEvent.KEY_RELEASED: {
				
				//Obtendo lista de key listeners
				KeyListener[] listeners = getKeyListeners();
				
				//Passando evento para os key listeners
				for (KeyListener listener : listeners) {
					listener.keyReleased(e);
				}
			}
		}
	}
	
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
}