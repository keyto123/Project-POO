package main;

import java.awt.Point;

import javax.swing.JButton;

/**
 * Main game button holding the index or row and column as a point
 * @author lucas
 *
 */
public class GameButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	private Point gamePosition;
	private PlayersIndex playerPlayed = PlayersIndex.NONE;
	
	public GameButton(String msg, Point position) {
		this.gamePosition = position;
	}
	
	public Point getGamePosition() {
		return this.gamePosition;
	}
	
	public void setPlayerIndex(PlayersIndex index) {
		playerPlayed = index;
	}
	
	public PlayersIndex getPlayerIndex() {
		return playerPlayed;
	}
}
