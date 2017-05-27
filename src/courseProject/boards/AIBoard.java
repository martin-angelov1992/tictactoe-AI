package courseProject.boards;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import coarseProject.Player;
import coarseProject.Position;

public class AIBoard {
	private Board board;
	private int value;

	public AIBoard(Board board) {
		this.board = board;
	}

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

		for (Player player : board.getPlayers()) {
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

	public AIBoard clone() {
		AIBoard newBoard = new AIBoard(board.clone());

		return newBoard;
	}

	// factory method
	public AIBoard with(Position successor) {
		AIBoard newBoard = new AIBoard(board.with(successor));

		return newBoard;
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

	// Iterator pattern
	private class MoveIterator implements Iterator<Position> {

		private byte x = 1;
		private byte y = 1;

		@Override
		public boolean hasNext() {
			// This call will mutate the iterator but still no blocks will be
			// skipped
			Position nextBlock = getNext();
			return nextBlock != null;
		}

		@Override
		public Position next() {
			Position nextBlock = getNext();
			mutate();
			return nextBlock;
		}

		private void mutate() {
			if (y == Board.BOARD_SIZE-1) {
				++x;
				y = 0;
			} else {
				++y;
			}
		}

		private Position getNext() {
			for (; x < Board.BOARD_SIZE; ++x) {
				for (; y < Board.BOARD_SIZE; ++y) {
					if (board.getSquares()[x][y] == 0) {
						return Position.getPosition(x, y);
					}
				}
			}

			return null;
		}
	}

	public Iterator<Position> movementIterator() {
		return new MoveIterator();
	}
}