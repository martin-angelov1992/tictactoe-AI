package coarseProject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;

import courseProject.boards.Board;

public class Timer {

	private View view;
	private int timeLeft;
	private boolean botTurn;
	private ScheduledExecutorService executor;

	public Timer(View view, boolean botTurn) {
		this.view = view;
		this.botTurn = botTurn;
	}

	public void run() {
		timeLeft = Board.MOVE_SECONDS;
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable decreaser = new Decreaser();
		executor.scheduleAtFixedRate(decreaser, 1, 1, TimeUnit.SECONDS);
		printText();
		this.executor = executor;
	}

	public void stop() {
		executor.shutdown();
	}

	private void printText() {
		JLabel label = view.getTimeInfo();
		String whoHas = botTurn ? "Bot has" : "You have";
		String text = whoHas + " " + timeLeft+ 
				" second" + (timeLeft == 1 ? "" : "s") + " to move";
		//System.out.println("setting text to "+text);
		label.setText(text);
		label.paintImmediately(label.getVisibleRect());
	}

	private class Decreaser implements Runnable {

		@Override
		public void run() {
			--timeLeft;

			printText();

			if (timeLeft == 0) {
				stop();
				view.notifyTimeOver(botTurn);
			}
		}
	}
}