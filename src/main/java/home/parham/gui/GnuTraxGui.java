package home.parham.gui;

import home.parham.cli.Commands;
import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.engine.GnuTrax;
import home.parham.core.player.Player;
import home.parham.core.player.PlayerSimple;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GnuTraxGui extends JFrame {

	private Tile[] tiles;
	private JPanel outerPanel;
	private java.util.List<ImagePanel> board;
	private Loading loading;
	TraxBoard traxBoard;
	Player player;

	public GnuTraxGui(){
		super("GnuTrax 1.0");
		setResizable(false);
		setPreferredSize(new Dimension(800, 480));
		loading = new Loading(this);
		loading.setVisible(false);
		board = new ArrayList<ImagePanel>();
		newGame();
	}

	private void newGame(){
		Commands.userNew();
		player = new PlayerSimple();
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
		// System.out.println("POS: x: " + x + " Y: " + y);
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
		// System.out.println(sb.toString());
		return sb.toString();
	}

	private void clearBoard(){
		for (ImagePanel ip : board) {
			ip.setImage(tiles[TraxBoard.EMPTY].getImage());
		}
	}

	private int noToDraw(int a){
		return (a == 8) ? 8 : a + 2;
	}

	private void drawBoard(){
		int colDiff = 0, rowDiff = 0;
		this.setVisible(false);
		outerPanel = new JPanel();
		board.clear();
		int noOfRowsToDraw = noToDraw(traxBoard.getRowSize());
		int noOfColsToDraw = noToDraw(traxBoard.getColSize());
		outerPanel.setLayout(new GridLayout(noOfRowsToDraw, noOfColsToDraw));
		ImagePanel innerPanel;

		if (noOfColsToDraw == 8 && traxBoard.getColSize() == 8) {
			colDiff = -1;
		} else if (noOfRowsToDraw == 8 && traxBoard.getRowSize() == 8) {
			rowDiff = -1;
		}

		for (int i = 0; i < noOfRowsToDraw; i++) {
			for (int j = 0; j < noOfColsToDraw; j++) {
				// TODO Why is it needed to swap i and j here??
				innerPanel = new ImagePanel(tiles[TraxBoard.EMPTY].getImage(),
						this, j - colDiff, i - rowDiff);
				outerPanel.add(innerPanel);
				board.add(innerPanel);
			}
		}
		this.getContentPane().remove(0);
		this.getContentPane().add(outerPanel);

		for (int i = 1; i <= traxBoard.getRowSize(); i++) {
			for (int j = 1; j <= traxBoard.getColSize(); j++) {
				board.get((i + rowDiff) * noOfColsToDraw + (j + colDiff)).setImage(
						tiles[traxBoard.getAt(i, j)].getImage());
			}
		}
		this.pack();
		this.setVisible(true);
	}

	private boolean checkForWinner(){
		if (traxBoard.isGameOver() != TraxStatus.NOPLAYER) {
			/*
			 * Show message box with winner and if you want to
			 * play again
			*/
			switch (traxBoard.isGameOver()) {
				case BLACK:
					showNewGameDialog("black");
					break;
				case WHITE:
					showNewGameDialog("white");
					break;
				default:
					showNewGameDialog("everyone");
			}
			return true;
		}
		return false;
	}

	private void showNewGameDialog(String winner){
		JOptionPane.showMessageDialog(this, "Good game. The winner was "
				+ winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
		newGameBoard();
	}

	private void makeAiMove(){
		new Thread(new Runnable() {
			@Override
			public void run(){
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run(){
						loading.setVisible(true);
					}
				});

				String aiMove = player.move(traxBoard);
				GnuTrax.getInstance().gotAMove(aiMove);

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run(){
						loading.setVisible(false);
						clearBoard();
						drawBoard();
						repaint();
						checkForWinner();
					}
				});
			}

		}).start();
	}

	public void setMove(int x, int y, Tile tile){
		boolean aiMayMove = false;
		String theMove = position(x, y, tile.getTileType());
		GnuTrax.getInstance().gotAMove(theMove);
		drawBoard();
		board.get(y * noToDraw(traxBoard.getColSize()) + x)
				.setImage(tile.getImage());
		clearBoard();
		drawBoard();
		aiMayMove = true;
		this.repaint();
		if (checkForWinner())
			return;
		if (aiMayMove) {
			makeAiMove();
			if (checkForWinner())
				return;
		}
	}

	public java.util.List<Tile> getPossibleTilesForPosition(int x, int y){
		java.util.List<Tile> possibleMoves = new ArrayList<Tile>();
		java.util.List<Integer> theMoves = traxBoard.getLegalTiles(x,
				y);
		for (Integer move : theMoves) {
			possibleMoves.add(tiles[move.intValue()]);
		}
		return possibleMoves;
	}

	private void newGameBoard(){
		board.clear();
		outerPanel = new JPanel();
		outerPanel.setLayout(new GridLayout(1, 1));
		ImagePanel innerPanel;

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 1; j++) {
				innerPanel = new ImagePanel(
						tiles[TraxBoard.INVALID].getImage(), this, j, i);
				outerPanel.add(innerPanel);
				board.add(innerPanel);
			}
		}
		board.get(0).setImage(tiles[TraxBoard.EMPTY].getImage());
		if (this.getContentPane().getComponentCount() > 0)
			this.getContentPane().remove(0);
		this.getContentPane().add(outerPanel);
		this.pack();
	}

	public void addComponentsToPane(final Container pane){
		tiles = new Tile[8];
		try {
			tiles[TraxBoard.NS] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/ns.gif")),
					TraxBoard.NS); // 80x80 gif
			tiles[TraxBoard.WE] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/we.gif")),
					TraxBoard.WE); // 80x80 gif
			tiles[TraxBoard.NW] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/nw.gif")),
					TraxBoard.NW); // 80x80 gif
			tiles[TraxBoard.NE] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/ne.gif")),
					TraxBoard.NE); // 80x80 gif
			tiles[TraxBoard.WS] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/ws.gif")),
					TraxBoard.WS); // 80x80 gif
			tiles[TraxBoard.SE] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/se.gif")),
					TraxBoard.SE); // 80x80 gif
			tiles[TraxBoard.INVALID] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/invalid.gif")),
					TraxBoard.INVALID); // 80x80 gif
			tiles[TraxBoard.EMPTY] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/blank.gif")),
					TraxBoard.EMPTY); // 80x80 gif
		} catch (IOException e) {
			e.printStackTrace();
		}
		newGameBoard();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method is invoked
	 */
	public static void createAndShowGUI(){
		// Create and set up the window.
		GnuTraxGui frame = new GnuTraxGui();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		frame.addComponentsToPane(frame.getContentPane());
		// Display the window.
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public static final long serialVersionUID = 2488472L;

}
