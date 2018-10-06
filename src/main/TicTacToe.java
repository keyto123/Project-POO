package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import communication.ProjectGame;
import communication.ProjectGameStats;

public class TicTacToe extends ProjectGame {
	private static final long serialVersionUID = 1L;

	private PlayersIndex currentPlayer;
	private PlayersIndex winner;

	private JLabel player;
	private JLabel status;
	private JLabel winLabel;

	private JPanel infoPanel;
	private JPanel bottomPanel;

	private GameButton gameButtons[][];
	private JButton restartButton;
	
	private int remainingPlays;

	public TicTacToe() {}

	@Override
	public boolean startIntro() {
		return false;
	}

	@Override
	public ProjectGame initGame(ProjectGameStats stats) {
		if (stats == null) {
			stats = this.getGameStats();
		} else {
			this.setGameStats(stats);
		}

		this.setLayout(new BorderLayout());

		currentPlayer = PlayersIndex.PLAYER;
		winner = PlayersIndex.NONE;
		
		this.player = new JLabel(currentPlayer.toString());
		this.status = new JLabel("Jogador " + stats.getWins() + " : " + stats.getLoses() + " Adversario");
		this.winLabel = new JLabel(winner.toString());
		
		infoPanel = new JPanel(new GridLayout());
		infoPanel.add(player);
		infoPanel.add(status);

		restartButton = new JButton("Restart");
		gameButtons = new GameButton[3][3];
		
		this.initButtons();
		
		
		bottomPanel = new JPanel();
		bottomPanel.add(restartButton);

		JPanel center = new JPanel(new GridLayout(3, 3));
		for (int i = 0; i < gameButtons.length; i++) {
			for (int j = 0; j < gameButtons[i].length; j++) {
				center.add(gameButtons[i][j]);
			}
		}

		this.add(infoPanel, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		remainingPlays = 9;

		return this;
	}

	@Override
	public boolean endGame() {
		return false;
	}

	@Override
	public ProjectGameStats getStats() {
		return this.getGameStats();
	}

	/**
	 * Check if the line specified has a winner
	 * @param line - line index to be checked
	 * @return - true if there is a winner at specified line
	 */
	private boolean checkLine(int line) {
		for (int i = 0; i < 2; i++) {
			PlayersIndex leftCheck = gameButtons[line][i].getPlayerIndex();
			PlayersIndex rightCheck = gameButtons[line][i + 1].getPlayerIndex();
			if (leftCheck == PlayersIndex.NONE || leftCheck != rightCheck) {
				return false;
			}
		}
		this.winner = gameButtons[line][0].getPlayerIndex();
		return true;
	}

	/**
	 * Check if a column specified has a winner
	 * @param column - column index to be checked
	 * @return - true if there is a winner at column
	 */
	private boolean checkColumn(int column) {
		for (int i = 0; i < 2; i++) {
			PlayersIndex upCheck = gameButtons[i][column].getPlayerIndex();
			PlayersIndex underCheck = gameButtons[i + 1][column].getPlayerIndex();
			if (upCheck == PlayersIndex.NONE || upCheck != underCheck) {
				return false;
			}
		}
		this.winner = gameButtons[0][column].getPlayerIndex();
		return true;
	}
	
	/**
	 * Check if main diagonal has a winner
	 * @return - true if there is a winner at main diagonal
	 */
	private boolean checkMainDiagonal() {
		for (int i = 0; i < 2; i++) {
			PlayersIndex leftCheck = gameButtons[i][i].getPlayerIndex();
			PlayersIndex rightCheck = gameButtons[i + 1][i + 1].getPlayerIndex();
			if (leftCheck == PlayersIndex.NONE || leftCheck != rightCheck) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if second diagonal has a winner
	 * @return - true if there is a winner at second diagonal
	 */
	private boolean checkSecondDiagonal() {
		for (int i = 0; i < 2; i++) {
			PlayersIndex leftCheck = gameButtons[2 - i][i].getPlayerIndex();
			PlayersIndex rightCheck = gameButtons[1 - i][i + 1].getPlayerIndex();
			
			if (leftCheck == PlayersIndex.NONE || leftCheck != rightCheck ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if there is a winner at diagonals
	 * @return - true if any diagonal has a winner
	 */
	private boolean checkDiagonals() {

		if(checkMainDiagonal()) {
			winner = gameButtons[0][0].getPlayerIndex();
			return true;
		}
		
		if(checkSecondDiagonal()) {
			winner = gameButtons[2][0].getPlayerIndex();
			return true;
		}

		return false;
	}

	/**
	 * Check if a location is at a diagonal, a diagonal is checked by two conditions:
	 * x == y, so that's the main diagonal and x + y == 2(max index of each row) for the second diagonal
	 * @param location - point to be checked
	 * @return - true if x == y or x + y == 2
	 */
	private boolean isAtDiagonal(Point location) {
		if ((location.x == location.y) || (location.x + location.y == 2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the last position played make someone a winner
	 * @param location - location of last play
	 * @return - true if someone is a winner, false otherwise
	 */
	private boolean checkWinner(Point location) {
		boolean hasWinner = checkLine(location.x);
		hasWinner = hasWinner || checkColumn(location.y);
		
		if (isAtDiagonal(location)) {
			hasWinner = hasWinner || checkDiagonals();
		}
		return hasWinner;
	}
	
	/**
	 * Just check if there is any available play
	 * @return - true if number of plays == 0, false otherwise
	 */
	private boolean checkDraw() {
		return remainingPlays == 0;
	}

	/**
	 * restart button listener
	 */
	private ActionListener restartListener = e -> {
		infoPanel.remove(winLabel);
		restartButton.setEnabled(false);

		for (GameButton[] line : gameButtons) {
			for (GameButton col : line) {
				col.setPlayerIndex(PlayersIndex.NONE);
				col.setText("");
				col.setEnabled(true);
			}
		}
		
		remainingPlays = 9;
		winner = PlayersIndex.NONE;
	};

	/**
	 * Set a finish state with every button disabled and restart enabled
	 */
	private void finishGame() {
		
		for(GameButton[] line : gameButtons) {
			for(GameButton col : line) {
				col.setEnabled(false);
			}
		}
		
		winLabel.setText("Vencedor: " + winner.toString());
		infoPanel.add(winLabel);
		restartButton.setEnabled(true);
	}
	
	/**
	 * Update the current player
	 */
	private void updatePlayer() {
		if (currentPlayer == PlayersIndex.PLAYER) {
			currentPlayer = PlayersIndex.ADVERSARY;
		} else {
			currentPlayer = PlayersIndex.PLAYER;
		}
		
		this.player.setText(currentPlayer.toString());
	}
	
	/**
	 * Update stats of the game like wins, loses, draws
	 */
	private void updateStats() {
		ProjectGameStats stats = this.getGameStats();
		
		if(winner == PlayersIndex.PLAYER) {
			stats.increaseWins();
		} else if(winner == PlayersIndex.ADVERSARY) {
			stats.increaseLoses();
		} else {
			stats.increaseDraws();
		}
		
		stats.increasePlayCount();
		status.setText("Jogador " + stats.getWins() + " : " + stats.getLoses() + " Adversario");
		
	}

	/**
	 * main listener for playable buttons
	 */
	private ActionListener gameButtonListener = e -> {
		GameButton b = (GameButton) e.getSource();
		Point location = b.getGamePosition();

		if (b.getPlayerIndex() != PlayersIndex.NONE) {
			return;
		}
		
		b.setEnabled(false);
		remainingPlays--;
		
		b.setPlayerIndex(currentPlayer);

		b.setText(currentPlayer.getLetter());
		updatePlayer();

		boolean hasWinner = checkWinner(location);

		if (hasWinner || checkDraw()) {
			finishGame();
			updateStats();
		}
	};

	/**
	 * initialize the set of playable buttons and restart button
	 */
	private void initButtons() {
		for (int i = 0; i < gameButtons.length; i++) {
			for (int j = 0; j < gameButtons[i].length; j++) {
				gameButtons[i][j] = new GameButton("", new Point(i, j));
				gameButtons[i][j].addActionListener(gameButtonListener);
			}
		}

		restartButton.addActionListener(restartListener);
	}

	@Override
	public String getGameName() { return "Tic Tac Toe"; }
}
