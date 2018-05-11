package pane;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/*
 * Essa classe é utilizada nas janelas InputPane e MultiInputPane para sinalizar que o usuário terminou de preencher os campos de texto
 */

public class FinishButton extends JButton {

	public FinishButton(Pane pane, int paneWidth, int top) {
		// Seta o texto do botão
		setText("OK");

		// Arruma a posição do botão na tela
		setBounds(new Rectangle(paneWidth / 2 - 37, top, 74, 25));

		// Adiciona um listener ao botão para "ouvir" os clicks do mouse
		addMouseListener(new MouseAdapter() {

			// Função que será rodada quando o botão for clickado
			public void mouseClicked(MouseEvent e) {
				
				// Seta o estado como 1, simbolizando para continuar o programa
				pane.setEstado(1);

				// Fecha a janela
				pane.dispose();
			}
		});
	}

}
