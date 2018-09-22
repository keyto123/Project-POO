package communication;

/**
 * Exception used for problems when increasing a stat
 * 
 * @author lucas
 *
 */
public class IncreaseStatException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The exception constructor, just calls super with hard coded error string
	 * @param statName - to be displayed with error
	 */
	public IncreaseStatException(String statName) {
		super(statName + " already at maximum possible value");
	}
}
