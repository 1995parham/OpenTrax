package home.parham.gui;

import javax.swing.*;
import java.awt.*;

public class Loading extends JDialog {
	private static final long serialVersionUID = 2221062376560774087L;

	public Loading(JFrame owner){
		super(owner, ModalityType.APPLICATION_MODAL);
		this.add(new Label("AI is thinking. Please wait...."));
		this.pack();
		int x = owner.getX() + (owner.getWidth() - getPreferredSize().width)
				/ 2;
		int y = owner.getY() + (owner.getHeight() - getPreferredSize().height)
				/ 2;
		setLocation(x, y);

		repaint();
	}
}
