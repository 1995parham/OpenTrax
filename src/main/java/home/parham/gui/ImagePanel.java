package home.parham.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
		java.util.List<Tile> possibleMoves = gnuTrax.getPossibleTilesForPosition(this.x, this.y);
		ChooseTile ct = new ChooseTile(this.gnuTrax, possibleMoves);
		ct.setVisible(true);
		if (ct.getChosenMove() != -1) {
			this.gnuTrax.setMove(this.x, this.y, possibleMoves.get(ct.getChosenMove()));
		}
	}

	public void setImage(BufferedImage image){
		this.image = image;
	}

	public BufferedImage getImage(){
		return this.image;
	}

	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}

	public static final long serialVersionUID = 14362462L;
}
