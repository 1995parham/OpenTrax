package home.parham.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

public class ChooseTile extends JDialog {

	private JPanel buttons;
	private int chosenMove;

	public ChooseTile(JFrame owner, List<Tile> possibleMoves){
		super(owner, "Choose Move", true);
		
		this.setLocationRelativeTo(owner);
		
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
			button.setSize(90, 90);
			button.setText("No moves available");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					ChooseTile.this.dispose();
				}
			});
			buttons.add(button);
		}
		this.add(buttons);
		this.pack();
	}

	public int getChosenMove(){
		return chosenMove;
	}

	private void addImageButton(Tile possibleMove, final int pos){
		JButton button;
		button = new JButton();
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(null);
		button.setSize(90, 90);
		button.setIcon(new ImageIcon(possibleMove.getImage()));
		button.setName("" + pos);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				ChooseTile.this.setVisible(false);
				chosenMove = pos;
			}
		});
		buttons.add(button);
	}

	private static final long serialVersionUID = 8281287412217260698L;

}
