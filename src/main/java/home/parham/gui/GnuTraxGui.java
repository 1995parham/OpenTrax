package home.parham.gui;

import home.parham.cli.Commands;
import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.engine.GnuTrax;
import home.parham.core.player.Player;
import home.parham.core.player.PlayerSimple;
import java.awt.Color;
import java.awt.Dimension;
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
	private boolean haveAI;

	public GnuTraxGui(){
		super("GnuTrax 1.0");
		setResizable(false);
		setPreferredSize(new Dimension(800, 480));
		board = new ArrayList<ImagePanel>();
		newGame();
	}

	private void newGame(){
		Commands.userNew();

		ChoosePlayer playerChooser = new ChoosePlayer(this);
		playerChooser.setVisible(true);
		int op = playerChooser.getChoosenOption();
		if (op == 1) {
			haveAI = true;
			player = new PlayerSimple();
		} else if (op == 0) {
			haveAI = false;
			player = null;
		}

		traxBoard = GnuTrax.getInstance().getTraxBoard();
		if (board != null && board.size() > 0) {
			for (int i = 0; i < 1; i++) {
				board.get(i).setImage(tiles[TraxBoard.INVALID].getImage());
			}
			board.get(0).setImage(tiles[TraxBoard.EMPTY].getImage());
			this.repaint();
		}
	}

	private String getRowColForPos(int x, int y){
		StringBuilder sb = new StringBuilder();
		switch (x) {
			case 0:
				sb.append("@");
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				sb.append(Character.toString((char) (x - 1 + 65)));
				break;
		}
		sb.append(y);
		return sb.toString();
	}

	private String position(int x, int y, int tileType){
		StringBuilder sb = new StringBuilder();
		sb.append(getRowColForPos(y, x));
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

	private void clearBoard(){
		for (ImagePanel imagePanel : board) {
			imagePanel.setImage(tiles[TraxBoard.EMPTY].getImage());
		}
	}

	private int noToDraw(int a){
		return (a == 8) ? 8 : a + 2;
	}

	private void drawBoard(){
		int colDiff = 0, rowDiff = 0;

		int noOfRowsToDraw = noToDraw(traxBoard.getRowSize());
		int noOfColsToDraw = noToDraw(traxBoard.getColSize());

		SpringLayout springLayout = new SpringLayout();

		board.clear();

		outerPanel = new JPanel();
		outerPanel.setLayout(springLayout);
		outerPanel.setBackground(new Color(0, 100, 0));

		ImagePanel innerPanel;

		if (noOfColsToDraw == 8 && traxBoard.getColSize() == 8) {
			colDiff = -1;
		} else if (noOfRowsToDraw == 8 && traxBoard.getRowSize() == 8) {
			rowDiff = -1;
		}

		int y = this.getPreferredSize().width / 2 - 40 * noOfColsToDraw;
		int x = this.getPreferredSize().height / 2 - 40 * noOfRowsToDraw;

		for (int i = 0; i < noOfRowsToDraw; i++) {
			for (int j = 0; j < noOfColsToDraw; j++) {
				innerPanel = new ImagePanel(tiles[TraxBoard.EMPTY].getImage(), this, i - rowDiff, j - colDiff);
				springLayout.putConstraint(SpringLayout.WEST, innerPanel, y + j * 80, SpringLayout.WEST, outerPanel);
				springLayout.putConstraint(SpringLayout.NORTH, innerPanel, x + i * 80, SpringLayout.NORTH, outerPanel);
				outerPanel.add(innerPanel);
				board.add(innerPanel);
			}
		}

		for (int i = 1; i <= traxBoard.getRowSize(); i++) {
			for (int j = 1; j <= traxBoard.getColSize(); j++) {
				board.get((i + rowDiff) * noOfColsToDraw + (j + colDiff)).setImage(tiles[traxBoard.getAt(i, j)].getImage());
				board.get((i + rowDiff) * noOfColsToDraw + (j + colDiff)).repaint();
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

	private void makeAIMove(){
		this.setEnabled(false);

		String AIMove = player.move(traxBoard);
		GnuTrax.getInstance().gotAMove(AIMove);

		clearBoard();
		drawBoard();
		checkForWinner();

		this.setEnabled(true);
	}

	public void setMove(int x, int y, Tile tile){
		String theMove = position(x, y, tile.getTileType());
		GnuTrax.getInstance().gotAMove(theMove);
		board.get(x * noToDraw(traxBoard.getColSize()) + y).setImage(tile.getImage());
		clearBoard();
		drawBoard();
		if (checkForWinner())
			return;

		if (haveAI)
			makeAIMove();

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

		ImagePanel innerPanel;

		innerPanel = new ImagePanel(tiles[TraxBoard.EMPTY].getImage(), this, 0, 0);
		int x = this.getPreferredSize().width / 2 - 40;
		int y = this.getPreferredSize().height / 2 - 40;
		springLayout.putConstraint(SpringLayout.NORTH, innerPanel, y, SpringLayout.NORTH, outerPanel);
		springLayout.putConstraint(SpringLayout.WEST, innerPanel, x, SpringLayout.WEST, outerPanel);
		outerPanel.add(innerPanel);
		board.add(innerPanel);
	}

	public void setTiles(){
		tiles = new Tile[8];
		try {
			/* 80x80 gif */
			tiles[TraxBoard.NS] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/ns.gif")), TraxBoard.NS);
			tiles[TraxBoard.WE] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/we.gif")), TraxBoard.WE);
			tiles[TraxBoard.NW] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/nw.gif")), TraxBoard.NW);
			tiles[TraxBoard.NE] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/ne.gif")), TraxBoard.NE);
			tiles[TraxBoard.WS] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/ws.gif")), TraxBoard.WS);
			tiles[TraxBoard.SE] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/se.gif")), TraxBoard.SE);
			tiles[TraxBoard.INVALID] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/invalid.gif")), TraxBoard.INVALID);
			tiles[TraxBoard.EMPTY] = new Tile(ImageIO.read(getClass().getClassLoader().getResource("images/large/blank.gif")), TraxBoard.EMPTY);
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
