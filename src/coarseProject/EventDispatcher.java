package coarseProject;

import java.util.LinkedList;
import java.util.List;

public class EventDispatcher {
	private static volatile EventDispatcher instance;
	private List<EventListener> eventListeners = new LinkedList<>();
	private List<TimeEventsListener> timeEventsListeners = new LinkedList<>();

	private EventDispatcher() {}

	public void addEventListener(EventListener listener) {
		eventListeners.add(listener);
	}

	public void addTimeEventListener(TimeEventsListener listener) {
		timeEventsListeners.add(listener);
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
		for (EventListener listener : eventListeners) {
			listener.madeBotMove(pos);
		}
	}

	public void madePlayerMove(Position pos) {
		for (EventListener listener : eventListeners) {
			listener.madePlayerMove(pos);
		}
	}

	public void gameStarted() {
		for (EventListener listener : eventListeners) {
			listener.gameStarted();
		}
	}

	public void gotWinner(Player winner) {
		for (EventListener listener : eventListeners) {
			listener.gotWinner(winner);
		}
	}

	public void onBotTurn() {
		for (EventListener listener : eventListeners) {
			listener.onBotTurn();
		}
	}

	public void onPlayerTurn() {
		for (EventListener listener : eventListeners) {
			listener.onPlayerTurn();
		}
	}

	public void onTimerTick(int timeLeft) {
		for (TimeEventsListener listener : timeEventsListeners) {
			listener.onTimerTick(timeLeft);
		}
	}

	public void onTimerEnd() {
		for (TimeEventsListener listener : timeEventsListeners) {
			listener.onTimerEnd();
		}
	}

	public void onDraw() {
		for (EventListener listener : eventListeners) {
			listener.onDraw();
		}
	}
}