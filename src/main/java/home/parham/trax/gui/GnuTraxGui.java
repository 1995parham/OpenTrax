package home.parham.trax.gui;

import home.parham.trax.cli.Commands;
import home.parham.trax.core.domain.ColumnRowGenerator;
import home.parham.trax.core.domain.TraxBoard;
import home.parham.trax.core.domain.TraxStatus;
import home.parham.trax.core.engine.GnuTrax;
import home.parham.trax.core.player.Player;
import home.parham.trax.core.player.PlayerSimple;
import home.parham.trax.core.player.ServerNetPlayer;
import home.parham.trax.core.util.TraxVersion;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GnuTraxGui extends JFrame {

	private Tile[] tiles;
	private JPanel outerPanel;
	private List<ImagePanel> board;
	private TraxBoard traxBoard;
	private Player player1;
	private Player player2;
	private boolean haveRemote1;
	private boolean haveRemote2;

	public GnuTraxGui(){
		super("GnuTrax " + TraxVersion.getVersion());

		setResizable(false);
		setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		board = new ArrayList<ImagePanel>();
		newGame();
	}

	private void newGame(){
		Commands.userNew();

		ChoosePlayer playerChooser1 = new ChoosePlayer(this);
		playerChooser1.setVisible(true);
		int op1 = playerChooser1.getChosenOption();
		if (op1 == 1) {
			haveRemote1 = true;
			player1 = new PlayerSimple();
		} else if (op1 == 0) {
			haveRemote1 = false;
			player1 = null;
		} else if (op1 == 2) {
			haveRemote1 = true;
			player1 = new ServerNetPlayer(1373);
		}

		ChoosePlayer playerChooser2 = new ChoosePlayer(this);
		playerChooser2.setVisible(true);
		int op2 = playerChooser2.getChosenOption();
		if (op2 == 1) {
			haveRemote2 = true;
			player2 = new PlayerSimple();
		} else if (op2 == 0) {
			haveRemote2 = false;
			player2 = null;
		} else if (op2 == 2) {
			haveRemote2 = true;
			player2 = new ServerNetPlayer(1374);
		}

		traxBoard = GnuTrax.getInstance().getTraxBoard();
		if (board != null && board.size() > 0) {
			board.get(0).setImage(tiles[TraxBoard.EMPTY].getImage());
			this.repaint();
		}

		this.setEnabled(false);
	}

	private String getRowColForPos(int x, int y){
		return ColumnRowGenerator.generate(y, x);
	}

	private String position(int x, int y, int tileType){
		StringBuilder sb = new StringBuilder();
		sb.append(getRowColForPos(x, y));
		switch (tileType) {
			case TraxBoard.NS:
			case TraxBoard.EW:
				sb.append("+");
				break;
			case TraxBoard.SE:
			case TraxBoard.WN:
				sb.append("/");
				break;
			case TraxBoard.NE:
			case TraxBoard.WS:
				sb.append("\\");
				break;
		}
		return sb.toString();
	}

	private int noToDraw(int no){
		return no + 2;
	}

	private void drawBoard(){

		int noOfRowsToDraw = noToDraw(traxBoard.getRowSize());
		int noOfColsToDraw = noToDraw(traxBoard.getColumnSize());

		SpringLayout springLayout = new SpringLayout();

		board.clear();

		outerPanel = new JPanel();
		outerPanel.setLayout(springLayout);
		outerPanel.setBackground(new Color(0, 195, 0));

		JLabel playerTurn = new JLabel(traxBoard.whoToMove().name());
		playerTurn.setForeground(Color.ORANGE);
		springLayout.putConstraint(SpringLayout.NORTH, playerTurn, 5, SpringLayout.NORTH, outerPanel);
		springLayout.putConstraint(SpringLayout.WEST, playerTurn, 5, SpringLayout.WEST, outerPanel);
		outerPanel.add(playerTurn);

		ImagePanel innerPanel;

		int y = this.getPreferredSize().width / 2 - 30 * noOfColsToDraw;
		int x = this.getPreferredSize().height / 2 - 30 * noOfRowsToDraw;

		for (int i = 0; i < noOfRowsToDraw; i++) {
			for (int j = 0; j < noOfColsToDraw; j++) {
				innerPanel = new ImagePanel(tiles[TraxBoard.EMPTY].getImage(), this, i, j);
				springLayout.putConstraint(SpringLayout.WEST, innerPanel, y + j * 60, SpringLayout.WEST, outerPanel);
				springLayout.putConstraint(SpringLayout.NORTH, innerPanel, x + i * 60, SpringLayout.NORTH, outerPanel);
				outerPanel.add(innerPanel);
				board.add(innerPanel);
			}
		}


		for (int i = 1; i <= traxBoard.getRowSize(); i++) {
			for (int j = 1; j <= traxBoard.getColumnSize(); j++) {
				board.get(i * noOfColsToDraw + j).setImage(tiles[traxBoard.getAt(i, j)].getImage());
				board.get(i * noOfColsToDraw + j).repaint();
			}
		}

		this.setContentPane(outerPanel);
		SwingUtilities.updateComponentTreeUI(this);
	}

	private boolean checkForWinner(){
		if (traxBoard.isGameOver() != TraxStatus.NOPLAYER) {
			/*
			 * Show message box with winner and if you want to
			 * play again
			*/
			switch (traxBoard.isGameOver()) {
				case BLACK:
					showEndGameDialog("black");
					break;
				case WHITE:
					showEndGameDialog("white");
					break;
				default:
					showEndGameDialog("everyone");
			}
			return true;
		}
		return false;
	}

	private void showEndGameDialog(String winner){
		JOptionPane.showMessageDialog(this, "Good game. The winner was "
				+ winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	public void startGame(){
		if (haveRemote1 && haveRemote2) {
			makeRemoteGame();
		}
		this.setEnabled(true);
	}

	private void makeRemoteGame(){
		this.setEnabled(false);
		String move = "";
		while (true) {
			move = makeRemoteMove(move, 1);
			move = makeRemoteMove(move, 2);
		}
	}

	private String makeRemoteMove(String otherPlayerMove, int playerNo){
		String AIMove = null;
		if (playerNo == 2)
			AIMove = player2.move(otherPlayerMove);
		if (playerNo == 1)
			AIMove = player1.move(otherPlayerMove);

		if (AIMove != null)
			GnuTrax.getInstance().gotAMove(AIMove);
		else
			System.exit(1);

		drawBoard();
		checkForWinner();

		return AIMove;
	}

	public void makeHumanMove(int x, int y, Tile tile){
		String theMove = position(x, y, tile.getTileType());
		GnuTrax.getInstance().gotAMove(theMove);

		drawBoard();

		if (checkForWinner())
			return;

		if (haveRemote2) {
			this.setEnabled(false);
			makeRemoteMove(theMove, 2);
			this.setEnabled(true);
		}

		checkForWinner();
	}

	public List<Tile> getPossibleTilesForPosition(int x, int y){
		List<Tile> possibleMoves = new ArrayList<Tile>();
		List<Integer> theMoves = traxBoard.getLegalTiles(x, y);
		if (theMoves == null)
			return possibleMoves;
		for (Integer move : theMoves) {
			possibleMoves.add(tiles[move]);
		}
		return possibleMoves;
	}

	private void newBoard(){
		SpringLayout springLayout = new SpringLayout();

		board.clear();

		outerPanel = new JPanel();
		outerPanel.setLayout(springLayout);
		outerPanel.setBackground(new Color(0, 195, 0));
		this.setContentPane(outerPanel);

		JLabel playerTurn = new JLabel(traxBoard.whoToMove().name());
		playerTurn.setForeground(Color.ORANGE);
		springLayout.putConstraint(SpringLayout.NORTH, playerTurn, 5, SpringLayout.NORTH, outerPanel);
		springLayout.putConstraint(SpringLayout.WEST, playerTurn, 5, SpringLayout.WEST, outerPanel);
		outerPanel.add(playerTurn);

		ImagePanel innerPanel;

		innerPanel = new ImagePanel(tiles[TraxBoard.EMPTY].getImage(), this, 0, 0);
		int x = this.getPreferredSize().width / 2 - 30;
		int y = this.getPreferredSize().height / 2 - 30;
		springLayout.putConstraint(SpringLayout.NORTH, innerPanel, y, SpringLayout.NORTH, outerPanel);
		springLayout.putConstraint(SpringLayout.WEST, innerPanel, x, SpringLayout.WEST, outerPanel);
		outerPanel.add(innerPanel);
		board.add(innerPanel);
	}

	public void setTiles(){
		tiles = new Tile[8];
		try {
			/* 60x60 gif */
			tiles[TraxBoard.NS] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/ns.gif")), TraxBoard.NS);
			tiles[TraxBoard.WE] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/we.gif")), TraxBoard.WE);
			tiles[TraxBoard.NW] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/nw.gif")), TraxBoard.NW);
			tiles[TraxBoard.NE] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/ne.gif")), TraxBoard.NE);
			tiles[TraxBoard.WS] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/ws.gif")), TraxBoard.WS);
			tiles[TraxBoard.SE] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/se.gif")), TraxBoard.SE);
			tiles[TraxBoard.INVALID] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/invalid.gif")), TraxBoard.INVALID);
			tiles[TraxBoard.EMPTY] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/medium/blank.gif")), TraxBoard.EMPTY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		newBoard();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method is invoked
	 */
	public static void createAndShowGUI(){
		/* Create and set up the window. */
		GnuTraxGui frame = new GnuTraxGui();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* Set up the content pane. */
		frame.setTiles();
		/* Display the window. */
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.startGame();
	}

	public static final long serialVersionUID = 2488472L;

}
