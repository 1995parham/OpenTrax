package home.parham.gui;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class ChoosePlayer extends JDialog {
	private JRadioButton humanButton;
	private JRadioButton simpleAIButton;

	private int choosenOption;

	public ChoosePlayer(JFrame owner){
		super(owner, "Choose Player", true);
		this.setLocationRelativeTo(owner);
		this.setSize(new Dimension(200, 300));

		SpringLayout springLayout = new SpringLayout();

		JPanel contentPane = new JPanel();
		contentPane.setLayout(springLayout);

		JButton buttonOK = new JButton("Ok");
		springLayout.putConstraint(SpringLayout.EAST, buttonOK, -5, SpringLayout.EAST, contentPane);
		springLayout.putConstraint(SpringLayout.SOUTH, buttonOK, -5, SpringLayout.SOUTH, contentPane);
		getRootPane().setDefaultButton(buttonOK);
		contentPane.add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				onOK();
			}
		});

		JButton buttonCancel = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.EAST, buttonCancel, -5, SpringLayout.WEST, buttonOK);
		springLayout.putConstraint(SpringLayout.SOUTH, buttonCancel, 0, SpringLayout.SOUTH, buttonOK);
		contentPane.add(buttonCancel);
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				onCancel();
			}
		});

		ButtonGroup playersButtons = new ButtonGroup();

		humanButton = new JRadioButton("Human Player");
		humanButton.setSelected(true);
		springLayout.putConstraint(SpringLayout.WEST, humanButton, 5, SpringLayout.WEST, contentPane);
		springLayout.putConstraint(SpringLayout.NORTH, humanButton, 10, SpringLayout.NORTH, contentPane);
		playersButtons.add(humanButton);
		contentPane.add(humanButton);

		simpleAIButton = new JRadioButton("Simple AI Player");
		springLayout.putConstraint(SpringLayout.WEST, simpleAIButton, 5, SpringLayout.WEST, contentPane);
		springLayout.putConstraint(SpringLayout.NORTH, simpleAIButton, 5, SpringLayout.SOUTH, humanButton);
		playersButtons.add(simpleAIButton);
		contentPane.add(simpleAIButton);

		this.setContentPane(contentPane);
		
		/* call onCancel() when cross is clicked */
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				onCancel();
			}
		});

		/* call onCancel() on ESCAPE */
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

	}

	private void onOK(){
		choosenOption = 0;
		if (humanButton.isSelected())
			choosenOption = 0;
		if (simpleAIButton.isSelected())
			choosenOption = 1;
		dispose();
	}

	private void onCancel(){
		choosenOption = 0;
		dispose();
	}

	public int getChoosenOption(){
		return choosenOption;
	}
}
