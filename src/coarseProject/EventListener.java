package coarseProject;

public interface EventListener {
	void madeBotMove(Position pos);
	void madePlayerMove(Position pos);
	void timesUp(Player winner);
	void gameStarted(Player whosFirst);
	void gotWinner(Player winner);
}
