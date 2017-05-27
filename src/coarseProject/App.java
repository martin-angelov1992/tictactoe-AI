package coarseProject;

public class App {
	public static void main(String[] args) {
		EventDispatcher eventDispatcher = EventDispatcher.getInstance();

		Game game = Game.getInstance();
		View view = View.getInstance();
		Logger logger = Logger.getInstance();

		eventDispatcher.addEventListener(logger);
		eventDispatcher.addEventListener(view);
		eventDispatcher.addTimeEventListener(game);
		eventDispatcher.addTimeEventListener(view);
		eventDispatcher.addTimeEventListener(logger);

		view.show();
	}
}