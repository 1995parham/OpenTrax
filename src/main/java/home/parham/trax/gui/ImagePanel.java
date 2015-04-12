package home.parham.trax.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JComponent;

public class ImagePanel extends JComponent {
	public static final long serialVersionUID = 14362462L;

	private GnuTraxGui gnuTrax;
	private int x, y;
	private Image image;

	ImagePanel(BufferedImage image, GnuTraxGui gnuTrax, int x, int y){
		this.setImage(image);
		this.gnuTrax = gnuTrax;
		this.x = x;
		this.y = y;

		this.addMouseListener(new ImageClickHandler());
		this.setPreferredSize((new Dimension(60, 60)));
	}

	public void showMovesDialog(){
		List<Tile> possibleMoves = gnuTrax.getPossibleTilesForPosition(this.x, this.y);
		ChooseTile ct = new ChooseTile(this.gnuTrax, possibleMoves);
		ct.setVisible(true);
		if (ct.getChosenMove() != -1) {
			this.gnuTrax.makeHumanMove(this.x, this.y, possibleMoves.get(ct.getChosenMove()));
		}
	}

	public void setImage(BufferedImage image){
		this.image = image;
		repaint();
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, 60, 60);
		g.drawImage(image, 0, 0, this);
	}

	private class ImageClickHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e){
			ImagePanel.this.showMovesDialog();
		}

		@Override
		public void mouseEntered(MouseEvent e){
		}

		@Override
		public void mouseExited(MouseEvent e){
		}

		@Override
		public void mousePressed(MouseEvent e){
		}

		@Override
		public void mouseReleased(MouseEvent e){
		}

	}

}
