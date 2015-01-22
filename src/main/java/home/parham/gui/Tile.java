package org.traxgame.gui;

import java.awt.image.BufferedImage;

public class Tile {
	
	private BufferedImage image;
	private int tileType;
	
	public Tile(BufferedImage image, int tileType) {
		this.image = image;
		this.tileType = tileType;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public int getTileType() {
		return this.tileType;
	}
}
