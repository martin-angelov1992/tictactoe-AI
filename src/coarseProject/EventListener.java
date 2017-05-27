package coarseProject;

public interface EventListener {
	void madeBotMove(Position pos);
	void madePlayerMove(Position pos);
	void gameStarted();
	void gotWinner(Player winner);
	void onBotTurn();
	void onPlayerTurn();
	void onDraw();
}