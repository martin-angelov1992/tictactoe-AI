package courseProject.boards;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import coarseProject.Player;
import coarseProject.Position;
import coarseProject.winnerchecks.DiagonalWinnerChecker;
import coarseProject.winnerchecks.HorizontalWinnerChecker;
import coarseProject.winnerchecks.VerticalWinnerChecker;
import coarseProject.winnerchecks.WinnerChecker;

public class Board {
	private byte[][] squares;
	private List<Player> players;
	private Player playerTurn;
	private static Set<WinnerChecker> checkers;

	public static final int MOVE_SECONDS = 4;
	public static final int BOARD_SIZE = 3;

	static {
		initCheckers();
	}

	public Board() {
		players = new LinkedList<>();
		squares = new byte[BOARD_SIZE][BOARD_SIZE];
		addDefaultPlayers();
	}

	private static void initCheckers() {
		checkers = new HashSet<>();
		checkers.add(new DiagonalWinnerChecker());
		checkers.add(new HorizontalWinnerChecker());
		checkers.add(new VerticalWinnerChecker());
	}

	public Player getPlayerTurn() {
		return playerTurn;
	}

	private void addDefaultPlayers() {
		Player me = Player.newPlayer("You", (byte)1);
		Player bot = Player.newBot("Bot", (byte)2);
		players.add(me);
		players.add(bot);
	}

	public List<Position> getAllActions() {
		List<Position> moves = new LinkedList<>();

		for (int i=0;i<Board.BOARD_SIZE;++i) {
			for (int j=0;j<Board.BOARD_SIZE;++j) {
				if (squares[i][j] == 0) {
					moves.add(Position.getPosition((byte)i, (byte)j));
				}
			}
		}

		return moves;
	}

	public Board clone() {
		Board board = new Board();

		board.players = this.players;
		board.squares = cloneSquares(this.squares);
		board.playerTurn = this.playerTurn;

		return board;
	}

	Board with(Position successor) {
		Board newBoard = new Board();

		newBoard.playerTurn = getOtherPlayer(playerTurn);
		newBoard.squares = cloneSquares(squares);
		newBoard.squares[successor.getX()][successor.getY()] = newBoard.playerTurn.getID();
		newBoard.players = players;

		return newBoard;
	}

	public static byte[][] cloneSquares(byte[][] squares) {
		byte[][] newSquares = new byte[squares.length][squares.length];

		for (int i=0;i<squares.length;++i) {
			for (int j=0;j<squares[i].length;++j) {
				newSquares[i][j] = squares[i][j];
			}
		}

		return newSquares;
	}

	public boolean validMove(Player player, Position pos) {
		if (player.getID() != playerTurn.getID()) {
			return false;
		}

		if (squares[pos.getX()][pos.getY()] != 0) {
			return false;
		}

		return true;
	}

	public Player getWinner() {
		return getWinner(getSquares());
	}

	Player getWinner(byte[][] squares) {
		for (WinnerChecker checker : checkers) {
			byte playerID = checker.getWinnerID(squares);
			if (playerID != 0) {
				return getPlayerByID(playerID);
			}
		}

		return null;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public Player getPlayerByID(int ID) {
		for (Player player : players) {
			if (player.getID() == ID) {
				return player;
			}
		}

		return null;
	}

	public Player getPlayer() {
		for (Player player : players) {
			if (!player.isBot()) {
				return player;
			}
		}

		return null;
	}

	public Player getBot() {
		for (Player player : players) {
			if (player.isBot()) {
				return player;
			}
		}

		return null;
	}

	public Player getOtherPlayer(Player player) {
		for (Player otherPlayer : players) {
			if (otherPlayer.getID() != player.getID()) {
				return otherPlayer;
			}
		}

		return null;
	}

	public void reset() {
		for (int i = 0; i < squares.length; ++i) {
			for (int j = 0; j < squares[i].length; ++j) {
				squares[i][j] = 0;
			}
		}
	}

	public boolean hasMoreMoves() {
		for (int i=0;i<squares.length;++i) {
			for (int j=0;j<squares[i].length;++j) {
				if (squares[i][j] == 0) {
					return true;
				}
			}
		}

		return false;
	}

	public byte[][] getSquares() {
		return squares;
	}

	List<Player> getPlayers() {
		return players;
	}

	public void setPlayerTurn(Player player) {
		playerTurn = player;
	}
}