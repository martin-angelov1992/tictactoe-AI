package coarseProject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import courseProject.boards.Board;

public class Timer {
	private boolean botTurn;
	private volatile int timeLeft;
	private ScheduledExecutorService executor;
	private EventDispatcher eventDispatcher;

	private static volatile Timer instance;

	private Timer() {}

	public void start(boolean botTurn) {
		this.botTurn = botTurn;
		if (executor != null) {
			executor.shutdownNow();
		}

		timeLeft = Board.MOVE_SECONDS;
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable decreaser = new Decreaser(executor);
		executor.scheduleAtFixedRate(decreaser, 1, 1, TimeUnit.SECONDS);
		this.executor = executor;
		eventDispatcher = EventDispatcher.getInstance();
		eventDispatcher.onTimerTick(timeLeft);
	}

	public void stop() {
		executor.shutdownNow();
	}

	private class Decreaser implements Runnable {
		private ScheduledExecutorService executor;

		public Decreaser(ScheduledExecutorService executor) {
			this.executor = executor;
		}

		@Override
		public void run() {
			if (executor.isShutdown()) {
				return;
			}

			--timeLeft;
			eventDispatcher.onTimerTick(timeLeft);

			if (timeLeft == 0) {
				stop();

				// Let the bot think ;)
				if (!botTurn) {
					//eventDispatcher.onTimerEnd();
				}
			}
		}
	}

	public static Timer getInstance() {
		if (instance == null) {
			synchronized(Timer.class) {
				if (instance == null) {
					instance = new Timer();
				}
			}
		}

		return instance;
	}
}