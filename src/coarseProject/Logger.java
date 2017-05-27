package coarseProject;

public class Logger implements EventListener, TimeEventsListener {

	private static volatile Logger instance;

	private Logger() {}

	@Override
	public void madeBotMove(Position pos) {
		someoneMadeMove("Bot", pos);
	}

	@Override
	public void madePlayerMove(Position pos) {
		someoneMadeMove("Player", pos);
	}

	private void someoneMadeMove(String who, Position pos) {
		System.out.printf("%s made move (%d, %d).\n", who, pos.getX(), pos.getY());
	}

	@Override
	public void gameStarted() {
		System.out.println("Game Started.");
	}

	@Override
	public void gotWinner(Player winner) {
		System.out.printf("%s won.\n", winner.getName());
	}

	@Override
	public void onBotTurn() {
		System.out.println("It's bot's turn.");
	}

	@Override
	public void onPlayerTurn() {
		System.out.println("It's your turn.");
	}

	public static Logger getInstance() {
		if (instance == null) {
			synchronized (Logger.class) {
				if (instance == null) {
					instance = new Logger();
				}
			}
		}

		return instance;
	}

	@Override
	public void onTimerTick(int timeLeft) {}

	@Override
	public void onTimerEnd() {
		System.out.println("Time ended.");
	}

	@Override
	public void onDraw() {
		System.out.println("It's draw.");
	}
}