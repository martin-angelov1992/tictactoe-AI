package courseProject.gamestates;

import coarseProject.Game;
import coarseProject.Position;

public class NotStartedState extends GameState {

	public NotStartedState(Game game) {
		super(game);
	}

	@Override
	public boolean validMove(Position pos) {
		return false;
	}
}