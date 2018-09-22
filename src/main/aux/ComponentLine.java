package main.aux;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ComponentLine extends JPanel {
	private static final long serialVersionUID = 1L;

	public ComponentLine() {
		// Esse BoxLayout já vem com o swing e mudando de X_AXIS pra Y_AXIS, mudamos de linha pra coluna
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	public ComponentLine(JComponent ...comp) {
		this();
		addComponents(comp);
	}
	
	public void addComponents(JComponent ...comp) {
		for(JComponent c : comp) {
			this.add(c);
		}
	}
}
