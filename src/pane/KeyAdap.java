package pane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/*
 * Essa classe é utilizada nos campos de texto das InputPanes para que quando o usuário tecle a tecla Enter o programa entenda que ele terminou de preencher o campo
 */

public class KeyAdap extends KeyAdapter {

	//Variáveis internas
	protected Pane pane;
	protected SmartField edit;
	protected String placeholder;
	
	//Construtores
	public KeyAdap(Pane pane) {
		this.pane = pane;
	}
	
	//Métodos
	//Função que será executada quando alguma tecla for apertada
	@Override
	public void keyPressed(KeyEvent evt) {
		
		//Variáveis internas
		int key = evt.getKeyCode();
		
		//Verificando se a tecla apertada é enter
		if (key == KeyEvent.VK_ENTER) {
			
			//Seta o estado como 1, simbolizando para continuar o programa
			pane.setEstado(1);
			
			//Fecha a janela
			pane.dispose();
		}
	}
}
