package coarseProject;

public interface TimeEventsListener {
	void onTimerTick(int timeLeft);
	void onTimerEnd();
}