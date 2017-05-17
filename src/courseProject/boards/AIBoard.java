package courseProject.boards;

import java.util.HashMap;
import java.util.Map;

import coarseProject.Player;

public class AIBoard {
	private Board board;
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}


	public Map<Player, Integer> getScores() {
		Map<Player, Integer> scores = new HashMap<>();

		for (Player player : players) {
			int winningSquares = getWinningSquaresFor(player);
			scores.put(player, winningSquares);
		}

		return scores;
	}

	public int getScore() {
		Player winner = board.getWinner();

		Player me = board.getPlayer();
		Player bot = board.getBot();

		if (winner != null) {
			if (winner.getID() == bot.getID()) {
				return 1000;
			}

			if (winner.getID() == me.getID()) {
				return -1000;
			}
		}

		Map<Player, Integer> scores = getScores();

		return scores.get(bot) - scores.get(me);
	}

	private int getWinningSquaresFor(Player player) {
		int winningSquares = 0;
		byte[][] squares = Board.cloneSquares(board.getSquares());

		for (int i=0;i<squares.length;++i) {
			for (int j=0;j<squares.length;++j) {
				if (squares[i][j] == 0) {
					squares[i][j] = player.getID();
					Player winner = board.getWinner(squares);

					if (winner != null && winner.getID() == player.getID()) {
						++winningSquares;
					}

					squares[i][j] = 0;
				}
			}
		}

		return winningSquares;
	}
}