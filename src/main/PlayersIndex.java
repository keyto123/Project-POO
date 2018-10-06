package main;

/**
 * enum for none, player or adversary to be used in the game comparisions and
 * informations
 * 
 * @author lucas
 *
 */
public enum PlayersIndex {
	NONE("", "Nenhum"), PLAYER("X", "Jogador"), ADVERSARY("O", "Adversario");

	private String letter;
	private String user;

	private PlayersIndex(String letter, String user) {
		this.letter = letter;
		this.user = user;
	}

	@Override
	public String toString() {
		return user;
	}

	public String getLetter() {
		return letter;
	}
}
