package main.aux;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ComponentColumn extends JPanel {
	private static final long serialVersionUID = 1L;

	public ComponentColumn() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public ComponentColumn(JComponent ...comp) {
		this();
		addComponents(comp);
	}
	
	public void addComponents(JComponent ...comp) {
		for(JComponent c : comp) {
			this.add(c);
		}
	}
}
