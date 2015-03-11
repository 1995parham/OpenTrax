package home.parham.gui;

import home.parham.cli.Commands;
import home.parham.core.domain.ColumnRowGenerator;
import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.engine.GnuTrax;
import home.parham.core.player.Player;
import home.parham.core.player.PlayerSimple;
import home.parham.core.player.ServerNetPlayer;
import home.parham.core.util.TraxVersion;
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
	private Player player;
	private boolean haveRemote;

	public GnuTraxGui(){
		super("GnuTrax " + TraxVersion.getVersion());

		setResizable(false);
		setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		board = new ArrayList<ImagePanel>();
		newGame();
	}

	private void newGame(){
		Commands.userNew();

		ChoosePlayer playerChooser = new ChoosePlayer(this);
		playerChooser.setVisible(true);
		int op = playerChooser.getChoosenOption();
		if (op == 1) {
			haveRemote = true;
			player = new PlayerSimple();
		} else if (op == 0) {
			haveRemote = false;
			player = null;
		} else if (op == 2) {
			haveRemote = true;
			player = new ServerNetPlayer();
		}

		traxBoard = GnuTrax.getInstance().getTraxBoard();
		if (board != null && board.size() > 0) {
			board.get(0).setImage(tiles[TraxBoard.EMPTY].getImage());
			this.repaint();
		}
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
		outerPanel.setBackground(new Color(0, 100, 0));

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

	private void makeRemoteMove(String otherPlayerMove){
		this.setEnabled(false);

		String AIMove = player.move(otherPlayerMove);
		if (AIMove != null)
			GnuTrax.getInstance().gotAMove(AIMove);

		drawBoard();
		checkForWinner();

		this.setEnabled(true);
	}

	public void setMove(int x, int y, Tile tile){
		String theMove = position(x, y, tile.getTileType());
		GnuTrax.getInstance().gotAMove(theMove);

		drawBoard();

		if (checkForWinner())
			return;

		if (haveRemote)
			makeRemoteMove(theMove);

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
		outerPanel.setBackground(new Color(0, 100, 0));
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
	}

	public static final long serialVersionUID = 2488472L;

}
