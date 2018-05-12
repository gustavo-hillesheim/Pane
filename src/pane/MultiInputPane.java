package pane;

import javax.swing.*;

/*
 * Essa classe é utilizada para receber múltiplas informações do usuário
 */

public class MultiInputPane extends Pane {

	private static final long serialVersionUID = 1L;
	
	//Variáveis internas
	private static SmartField[] edits;
	
	// Construtor
	public MultiInputPane(String titulo, String texto, Object[][] inputs, int width) {
	
		// Construtor do pane
		super(titulo, texto, width);

		// Re-setando variável width
		width = (int) getBounds().getWidth();

		adicionarEdits(this, inputs);

		terminarJanela(width, inputs.length);
	}
	
	//Métodos
	public void desenharPlaceholders() {
		
		for (SmartField edit : edits) {
			edit.desenharPlaceholder();
		}
	}
	
	public static Object[] input(String titulo, String texto, Object[][]inputs, int width) {

		//Cria uma janela de acordo com as configurações específicadas
		MultiInputPane pane = new MultiInputPane(titulo, texto, inputs, width);
		
		//Eperando janela renderizar
		try {
			Thread.sleep(100);
		} catch(Exception e) {}
		
		//Desenhando placeholders
		pane.desenharPlaceholders();
		
		Object[] vetor = coletarDados(pane, inputs.length, inputs);
		
		//Retorna o vetor
		return vetor;
	}
	
	private static Object[] coletarDados(MultiInputPane pane, int length, Object[][] inputs) {
		
		//Vetor que será retornado
		Object[] vetor = new Object[length];
		
		//Laço para verificar se nenhum edit ficou vazio
		boolean algoVazio = true;
		while (algoVazio) {
			
			algoVazio = false;
			pane.setVisible(true);
			pane.setEstado(0);
			
			//Espera o usuário terminar de inserir suas informações
			esperaTerminar(pane);
			
			//Coleta as informações inseridas
			for (int i = 0; i < length; i++) {
				
				//Variável que recebe a informação no edit
				String text = edits[i].getText();
				
				algoVazio = verificarEdit(text, edits[i]);
				
				vetor[i] = retornarValor(text, inputs[i][1].toString());
				
			}
		}
		
		return vetor;
	}
	
	private static Object retornarValor(String text, String tipo) {
		
		//Verificando se é necessário converter
		switch (tipo) {
		
			//Caso não precise apenas retorna um string
			case "STRING":
				return text;
			
			//Retorna Integer caso necessário
			case "INT":
				return Integer.parseInt(text);
			
			//Retorna Double caso necessário
			case "DOUBLE":
				return Double.parseDouble(text);
		}
		
		//Caso não seja de nenhum tipo aceito retorna nulo
		return null;
	}

	private static boolean verificarEdit(String text, SmartField edit) {
		
		edit.resetCores();
		
		//Verifica se o campo está vazio
		if (text.isEmpty()) {
			
			edit.sinalizarErro();
			return true;
		}
		
		return false;
	}

	private void adicionarEdits(MultiInputPane pane, Object[][] inputs) {
		
		// Iniciando array
		edits = new SmartField[inputs.length];

		//Adicionando edits à janela
		for (int i = 0; i < inputs.length; i++) {

			//Obtendo caracteres permitidos
			String caracteresPermitidos = "";
					
			iniciarEdit(i, inputs);
					
			SmartField edit = edits[i];

			//Configurando posição do texto
			edit.setHorizontalAlignment(SwingConstants.CENTER);
					
			//Arrumando posição e tamanho do edit
			edit.setBounds(50, 35 * (i + 1) + lblHeight, width - 100, 25);

			//Adicionando edit à janela
			pane.add(edit);
		}
	}
	
	private void iniciarEdit(int i, Object[][] inputs) {
		
		//Verificando tipo
		switch (inputs[i][1].toString().toUpperCase()) {
			
			case "STRING": {
				
				//Verificando se foi passado uma parâmetro extra
				if (inputs[i].length > 2) {
					
					//Iniciando edit com caracteres permitidos personalizados
					edits[i] = new SmartField(inputs[i][0].toString(), inputs[i][2].toString());
				} else {
					
					//Inciando edit padrão
					edits[i] = new SmartField(inputs[i][0].toString());
				}
			}
			
			default: {
				
				boolean decimal = inputs[i][1].toString().equals("DOUBLE") ? true : false;
				
				//Verificando se foi passado mínimo e máximo para esse edit
				if (inputs[i].length > 2) {
					
					//Atribuindo valores
					double min = (double) inputs[i][2];
					double max = (double) inputs[i][3];
					
					//Iniciando edit com limites personalizados
					edits[i] = new SmartField(decimal, min, max);
				} else {
					
					//Iniciando edit padrão
					edits[i] = new SmartField(decimal);
				}
				break;
			}
		}
	}

	private void terminarJanela(int width, int ilength) {
		
		//Adicionando botão para finalizar
		add(new FinishButton(this, width, 35 * (ilength + 1) + lblHeight));
				
		//Arrumando tamanho da tela (75 seria o tamanho do botão mais 50 de margem)
		setBounds(0, 0, width,  35 * (ilength + 1) + lblHeight + 75);

		// Seta para a janela aparecer no meio da tela
		setLocationRelativeTo(null);

		// Deixa a janela visível
		setVisible(true);
	}
}