package coarseProject;

import courseProject.boards.Board;
import courseProject.gamestates.BotTurnState;
import courseProject.gamestates.GameOverState;
import courseProject.gamestates.GameState;
import courseProject.gamestates.NotStartedState;
import courseProject.gamestates.PlayerTurnState;
import courseProject.movementProviders.MovementProvider;

public class Game implements TimeEventsListener {
	private Board board;
	private Timer timer = Timer.getInstance();
	private EventDispatcher eventDispatcher = EventDispatcher.getInstance();
	private static volatile Game instance;
	private static MovementProvider movementProvider = MovementProvider.getDefaultInstance();

	private GameState state;
	private BotTurnState botTurnState;
	private PlayerTurnState playerTurnState;
	private GameOverState gameOverState;
	private NotStartedState notStartedState;

	private Game() {
		botTurnState = new BotTurnState(this);
		playerTurnState = new PlayerTurnState(this);
		gameOverState = new GameOverState(this);
		notStartedState = new NotStartedState(this);

		state = notStartedState;
	}

	public void stopGame() {
		Timer.getInstance().stop();
	}

	public void startNewGame(boolean botFirst) {
		board = new Board();
		board.setPlayerTurn(botFirst ? board.getBot() : board.getPlayer());

		EventDispatcher eventDispatcher = EventDispatcher.getInstance();

		eventDispatcher.gameStarted();

		Timer timer = Timer.getInstance();
		timer.start(botFirst);

		notifyPlayerTurn();

		if (botFirst) {
			Runnable r = () -> {
				makeBotMove();
				notifyPlayerTurn();

				Timer.getInstance().stop();
				Timer.getInstance().start(false);

				state = playerTurnState;
			};

			state = botTurnState;
			new Thread(r).start();
		} else {
			state = playerTurnState;
		}
	}

	private void makeBotMove() {
		timer.start(true);
		Position pos = movementProvider.getAction(board);

		makeMove(getBot(), pos);

		eventDispatcher.madeBotMove(pos);
	}

	public void makeMove(Player player, Position pos) {
		board.getSquares()[pos.getX()][pos.getY()] = player.getID();
		Player otherPlayer = board.getOtherPlayer(player);
		board.setPlayerTurn(otherPlayer);

		timer.stop();
	}

	private void notifyPlayerTurn() {
		if (board.getPlayerTurn().isBot()) {
			eventDispatcher.onBotTurn();
		} else {
			eventDispatcher.onPlayerTurn();
		}		
	}

	private Player checkForWinner() {
		Player winner = board.getWinner();

		if (winner == null) {
			return null;
		}

		eventDispatcher.gotWinner(winner);

		state = gameOverState;

		return winner;
	}

	// Returns true if the game continues. False otherwise.
	private boolean onMoveMade() {
		Player winner = checkForWinner();

		if (winner != null) {
			stopGame();
			return false;
		}

		notifyPlayerTurn();
		timer.start(board.getPlayerTurn().isBot());

		if (!board.hasMoreMoves()) {
			stopGame();
			eventDispatcher.onDraw();
			state = gameOverState;
			return false;
		}

		if (board.getPlayerTurn().isBot()) {
			state = botTurnState;
		} else {
			state = playerTurnState;
		}

		return true;
	}

	public boolean tryMakePlayerMove(Position pos) {
		Player player = board.getPlayer();

		if (!validMove(pos)) {
			return false;
		}

		makeMove(player, pos);
		eventDispatcher.madePlayerMove(pos);

		boolean continues = onMoveMade();

		if (!continues) {
			return true;
		}

		Runnable r = () -> {
			makeBotMove();
			onMoveMade();
		};

		new Thread(r).run();

		return true;
	}

	private boolean validMove(Position pos) {
		return state.validMove(pos);
	}

	@Override
	public void onTimerTick(int timeLeft) {}

	@Override
	public void onTimerEnd() {
		state = gameOverState;
		stopGame();
	}

	public static Game getInstance() {
		if (instance == null) {
			synchronized(Game.class) {
				if (instance == null) {
					instance = new Game();
				}
			}
		}

		return instance;
	}

	public Player getBot() {
		return board.getBot();
	}

	public Player getPlayer() {
		return board.getPlayer();
	}

	public Player getPlayerTurn() {
		return board.getPlayerTurn();
	}

	public Board getBoard() {
		return board;
	}
}