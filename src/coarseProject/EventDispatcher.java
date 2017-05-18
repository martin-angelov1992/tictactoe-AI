package coarseProject;

import java.util.LinkedList;
import java.util.List;

public class EventDispatcher {
	private static volatile EventDispatcher instance;
	private List<EventListener> listeners = new LinkedList<>();

	private EventDispatcher() {}

	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	public static EventDispatcher getInstance() {
		if (instance == null) {
			synchronized(EventDispatcher.class) {
				if (instance == null) {
					instance = new EventDispatcher();
				}
			}
		}

		return instance;
	}

	public void madeBotMove(Position pos) {
		for (EventListener listener : listeners) {
			listener.madeBotMove(pos);
		}
	}

	public void madePlayerMove(Position pos) {
		for (EventListener listener : listeners) {
			listener.madePlayerMove(pos);
		}
	}

	public void gameStarted() {
		for (EventListener listener : listeners) {
			listener.gameStarted();
		}
	}

	public void gotWinner(Player winner) {
		for (EventListener listener : listeners) {
			listener.gotWinner(winner);
		}
	}

	public void onBotTurn() {
		for (EventListener listener : listeners) {
			listener.onBotTurn();
		}
	}

	public void onPlayerTurn() {
		for (EventListener listener : listeners) {
			listener.onPlayerTurn();
		}
	}
}