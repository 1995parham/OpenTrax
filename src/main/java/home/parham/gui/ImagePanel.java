package home.parham.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private BufferedImage image;
	private GnuTraxGui gnuTrax;
	private int x, y;

	ImagePanel(BufferedImage image, GnuTraxGui gnuTrax, int x, int y){
		this.setImage(image);
		this.gnuTrax = gnuTrax;
		this.addMouseListener(new ImageClickHandler());
		this.setPreferredSize((new Dimension(80, 80)));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.x = x;
		this.y = y;
	}

	public void showMovesDialog(){
		List<Tile> possibleMoves = gnuTrax.getPossibleTilesForPosition(this.x, this.y);
		ChooseTile ct = new ChooseTile(this.gnuTrax, possibleMoves);
		ct.setVisible(true);
		if (ct.getChosenMove() != -1) {
			this.gnuTrax.setMove(this.x, this.y, possibleMoves.get(ct.getChosenMove()));
		}
	}

	public void setImage(BufferedImage image){
		this.image = image;
	}

	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}

	public static final long serialVersionUID = 14362462L;
}
