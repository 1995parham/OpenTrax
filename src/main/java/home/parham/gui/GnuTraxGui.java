package org.traxgame.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.imageio.*;
import org.traxgame.main.*;

public class GnuTraxGui extends JFrame {

	private Tile[] tiles;
	private JPanel outerPanel;
	private java.util.List<ImagePanel> board;
	private GnuTrax gnuTraxGame;
	private Loading loading;

	public GnuTraxGui() {
		super("GnuTrax 1.0");
		setResizable(false);
		setMinimumSize(new Dimension(80, 80));
		// setMaximumSize(new Dimension(80, 80));
		loading = new Loading(this);
		loading.setVisible(false);
		board = new ArrayList<ImagePanel>();
		newGame("simple");
	}

	private void newGame(String ai) {
		this.gnuTraxGame = new GnuTrax(ai);
		this.gnuTraxGame.userNew();
		if (board != null && board.size() > 0) {
			for (int i = 0; i < 1; i++) {
				board.get(i).setImage(tiles[Traxboard.INVALID].getImage());
			}
			board.get(0).setImage(tiles[Traxboard.EMPTY].getImage());
			this.repaint();
		}
	}

	private String getRowColForPos(int x, int y) {
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

	private String position(int x, int y, int tileType) {
		StringBuilder sb = new StringBuilder();
		sb.append(getRowColForPos(x, y));
		switch (tileType) {
		case Traxboard.NS:
		case Traxboard.EW:
			sb.append("+");
			break;
		case Traxboard.SE:
		case Traxboard.WN:
			sb.append("/");
			break;
		case Traxboard.NE:
		case Traxboard.WS:
			sb.append("\\");
			break;
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}

	private void clearBoard() {
		for (ImagePanel ip : board) {
			ip.setImage(tiles[Traxboard.EMPTY].getImage());
		}
	}

	private int noToDraw(int a) {
		return (a == 8) ? 8 : a + 2;
	}

	private void drawBoard() {
		int colDiff = 0, rowDiff = 0;
		this.setVisible(false);
		outerPanel = new JPanel();
		board.clear();
		int noOfRowsToDraw = noToDraw(this.gnuTraxGame.getBoardRows());
		int noOfColsToDraw = noToDraw(this.gnuTraxGame.getBoardCols());
		outerPanel.setLayout(new GridLayout(noOfRowsToDraw, noOfColsToDraw));
		ImagePanel innerPanel;
		
		if (noOfColsToDraw == 8 && this.gnuTraxGame.getBoardCols() == 8) {
			colDiff = -1;
		} else if (noOfRowsToDraw == 8 && this.gnuTraxGame.getBoardRows() == 8) {
			rowDiff = -1;
		}

		for (int i = 0; i < noOfRowsToDraw; i++) {
			for (int j = 0; j < noOfColsToDraw; j++) {
				// TODO Why is it needed to swap i and j here??
				innerPanel = new ImagePanel(tiles[Traxboard.EMPTY].getImage(),
						this, j-colDiff, i-rowDiff);
				innerPanel.setSize(new Dimension(80, 80));
				outerPanel.add(innerPanel);
				board.add(innerPanel);
			}
		}
		this.getContentPane().remove(0);
		this.getContentPane().add(outerPanel);

		for (int i = 1; i <= this.gnuTraxGame.getBoardRows(); i++) {
			for (int j = 1; j <= this.gnuTraxGame.getBoardCols(); j++) {
				board.get((i+rowDiff) * noOfColsToDraw + (j+colDiff)).setImage(
						tiles[this.gnuTraxGame.getTileAt(i, j)].getImage());
			}
		}
		this.pack();
		this.setVisible(true);
	}

	private boolean checkForWinner() {
		if (this.gnuTraxGame.isGameOver() != Traxboard.NOPLAYER) {
			// Show message box with winner and if you want to
			// play again
			switch (this.gnuTraxGame.isGameOver()) {
			case Traxboard.BLACK:
				showNewGameDialog("black");
				break;
			case Traxboard.WHITE:
				showNewGameDialog("white");
				break;
			default:
				showNewGameDialog("everyone");
			}
			return true;
		}
		return false;
	}

	private void showNewGameDialog(String winner) {
		JOptionPane.showMessageDialog(this, "Good game. The winner was "
				+ winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
		showAndChooseAi();
		newGameBoard();
	}

	private void makeAiMove() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						loading.setVisible(true);
					}
				});

				try {
					String aiMove = gnuTraxGame.makeComputerMove();
					gnuTraxGame.gotAMove(aiMove);
				} catch (IllegalMoveException ime) {
					System.out
							.println("AI made an illegal move... very strange");
				}

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
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

	public void setMove(int x, int y, Tile tile) {
		boolean aiMayMove = false;
		String theMove = position(x, y, tile.getTileType());
		try {
			this.gnuTraxGame.gotAMove(theMove);
			drawBoard();
			board.get(y * noToDraw(this.gnuTraxGame.getBoardCols()) + x)
					.setImage(tile.getImage());
			clearBoard();
			drawBoard();
			aiMayMove = true;
			this.repaint();
			if (checkForWinner())
				return;
		} catch (IllegalMoveException ime) {
			// Show message to the user and say try again
			System.out.println("ERROR " + ime.getMessage() + "\nThe move: "
					+ theMove);
		}
		if (aiMayMove) {
			makeAiMove();
			if (checkForWinner())
				return;
		}
		// System.out.println(this.gnuTraxGame.getTheBoard());
	}

	public java.util.List<Tile> getPossibleTilesForPosition(int x, int y) {
		java.util.List<Tile> possibleMoves = new ArrayList<Tile>();
		java.util.List<Integer> theMoves = this.gnuTraxGame.getPossibleMoves(x,
				y);
		for (Integer move : theMoves) {
			possibleMoves.add(tiles[move.intValue()]);
		}
		return possibleMoves;
	}

	private void newGameBoard() {
		board.clear();
		outerPanel = new JPanel();
		outerPanel.setLayout(new GridLayout(1, 1));
		ImagePanel innerPanel;

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 1; j++) {
				innerPanel = new ImagePanel(
						tiles[Traxboard.INVALID].getImage(), this, j, i);
				outerPanel.add(innerPanel);
				board.add(innerPanel);
			}
		}
		board.get(0).setImage(tiles[Traxboard.EMPTY].getImage());
		if (this.getContentPane().getComponentCount() > 0)
			this.getContentPane().remove(0);
		this.getContentPane().add(outerPanel);
		this.pack();
	}

	public void addComponentsToPane(final Container pane) {
		tiles = new Tile[8];
		try {

			tiles[Traxboard.NS] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/ns.gif")),
					Traxboard.NS); // 80x80 gif
			tiles[Traxboard.WE] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/we.gif")),
					Traxboard.WE); // 80x80 gif
			tiles[Traxboard.NW] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/nw.gif")),
					Traxboard.NW); // 80x80 gif
			tiles[Traxboard.NE] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/ne.gif")),
					Traxboard.NE); // 80x80 gif
			tiles[Traxboard.WS] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/ws.gif")),
					Traxboard.WS); // 80x80 gif
			tiles[Traxboard.SE] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/se.gif")),
					Traxboard.SE); // 80x80 gif
			tiles[Traxboard.INVALID] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/invalid.gif")),
					Traxboard.INVALID); // 80x80 gif
			tiles[Traxboard.EMPTY] = new Tile(ImageIO.read(getClass()
					.getClassLoader().getResource("images/large/blank.gif")),
					Traxboard.EMPTY); // 80x80 gif
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * outerPanel = new JPanel(); outerPanel.setLayout(new GridLayout(1,
		 * 1)); ImagePanel innerPanel;
		 * 
		 * for (int i = 0; i < 1; i++) { for (int j = 0; j < 1; j++) {
		 * innerPanel = new ImagePanel( tiles[Traxboard.INVALID].getImage(),
		 * this, j, i); outerPanel.add(innerPanel); board.add(innerPanel); } }
		 * board.get(0).setImage(tiles[Traxboard.EMPTY].getImage());
		 * pane.add(outerPanel);
		 */
		newGameBoard();
		showAndChooseAi();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method is invoked
	 * from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
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

	private void showAndChooseAi() {
		Object[] possibilities = { "simple (easy)", "uct (hard)", "quit" };
		int s = JOptionPane.showOptionDialog(this, "Choose AI:", "New game",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, possibilities, possibilities[0]);
		if (s == 2)
			System.exit(0);
		newGame(((String) possibilities[s]).split(" ")[0]);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static final long serialVersionUID = 2488472L;

}
