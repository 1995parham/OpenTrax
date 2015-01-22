package org.traxgame.gui;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ImageClickHandler implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		((ImagePanel)e.getSource()).showMovesDialog();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
