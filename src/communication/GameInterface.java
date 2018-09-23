package communication;

/**
 * Main game interface that should be used if a game is created to be playable
 * through the master interface Contains basic and necessary functions that let
 * a gui communicates with the game
 * 
 * @author lucas
 */
public interface GameInterface {
	/**
	 * Function to setup the intro of the game if it have it
	 * 
	 * @return - true if it has intro and can load it, false if fails any
	 */
	public boolean startIntro();

	/**
	 * 
	 * @param stats - object to be used as stats, must handle the case of being null
	 * @return - the game with it's initialized content
	 */
	public ProjectGame initGame(ProjectGameStats stats);

	/**
	 * Should update the stats that weren't updated yet
	 * 
	 * @return - true if it updated correctly or doesn't need to do update, false if
	 *         failed to update stats
	 */
	public boolean endGame();

	/**
	 * Get the current stats of the game like wins/loses/play count
	 * 
	 * @return ProjectGameStats - stats of the game
	 */
	public ProjectGameStats getStats();
	
	/**
	 * Get the name that will be displayed at master interface
	 */
	public String getGameName();
}
