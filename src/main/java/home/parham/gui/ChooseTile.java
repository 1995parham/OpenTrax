package org.traxgame.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChooseTile extends JDialog {

	private java.util.List<Tile> possibleMoves;
	private JPanel buttons;
	private int chosenMove;
	
	public ChooseTile(JFrame owner, java.util.List<Tile> possibleMoves) {
		super(owner, "Choose move", true);
		this.possibleMoves = possibleMoves;
		buttons = new JPanel();
		chosenMove = -1;
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		for (int i = 0; i < possibleMoves.size(); i++) {
			this.addImageButton(possibleMoves.get(i), i);
		}
		if (possibleMoves.size() == 0) {
			JButton button;
			button = new JButton();
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setBorder(null);
			button.setSize(90,90);
			button.setText("No moves available");
			button.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	ChooseTile.this.setVisible(false);
	            }
	        });
			buttons.add(button);
		}
		this.add(buttons);
		this.pack();
	}

	public int getChosenMove() {
		return chosenMove;
	}
	
	private void addImageButton(Tile possibleMove, final int pos) {
		JButton button;
		button = new JButton();
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(null);
		button.setSize(90,90);
		button.setIcon(new ImageIcon(possibleMove.getImage()));
		button.setName(""+pos);
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	ChooseTile.this.setVisible(false);
            	chosenMove = pos;
            }
        });
		buttons.add(button);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8281287412217260698L;

}
