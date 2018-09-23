package communication;

import javax.swing.JPanel;

/**
 * Main game class which should be used when creating new games
 * Usage is really simple, just extends it.
 * If necessary, setGameStats with a pre configured, otherwise
 * just stick with plain created stats
 * @author Lucas Mateus
 *
 */
public abstract class ProjectGame extends JPanel implements GameInterface {
	private static final long serialVersionUID = 1L;
	
	private ProjectGameStats gameStats;
	
	/**
	 * Create a new game stats to be used
	 */
	public ProjectGame() {
		this.gameStats = new ProjectGameStats();
	}
	
	/**
	 * Function to get current game stats
	 * @return gameStats - class with the stats and functions to increase them
	 */
	protected ProjectGameStats getGameStats() {
		return gameStats;
	}
	
	/**
	 * Function to update game stats with a new stats
	 * @param gameStats - object that will be used as game stats
	 */
	protected void setGameStats(ProjectGameStats gameStats) {
		this.gameStats = gameStats;
	}
}
