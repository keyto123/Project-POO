package communication;

/**
 * Main Game stats class, used for maintaining game stats
 * that can be passed to Master Interface
 * 
 * @author lucas
 *
 */
public class ProjectGameStats {
	private int wins = 0, loses = 0, draws = 0, playCount = 0;
	
	/**
	 * Increase {wins} value by one if it's not at maximum value
	 * @throws IncreaseStatException - runtime exception
	 */
	public void increaseWins() throws IncreaseStatException {
		if(wins >= Integer.MAX_VALUE) {
			throw new IncreaseStatException("wins");
		} else {
			this.wins++;
		}
	}
	
	public int getWins() { return wins; }
	public int getLoses() { return loses; }
	public int getDraws() { return draws; }
	public int getPlayCount() { return playCount; }

	/**
	 * Increase {loses} value by one if it's not at maximum value
	 * @throws IncreaseStatException
	 */
	public void increaseLoses() throws IncreaseStatException {
		if(loses >= Integer.MAX_VALUE) {
			throw new IncreaseStatException("loses");
		} else {
			this.loses++;
		}
	}
	
	/**
	 * Increase {draws} value by one if it's not at maximum value
	 * @throws IncreaseStatException - runtime exception
	 */
	public void increaseDraws() throws IncreaseStatException {
		if(draws >= Integer.MAX_VALUE) {
			throw new IncreaseStatException("draws");
		} else {
			this.draws++;
		}
	}
	
	/**
	 * Increase {play count} value by one if it's not at maximum value
	 * @throws IncreaseStatException - runtime exception
	 */
	public void increasePlayCount() throws IncreaseStatException {
		if(playCount >= Integer.MAX_VALUE) {
			throw new IncreaseStatException("play count");
		} else {
			this.playCount++;
		}
	}
}
