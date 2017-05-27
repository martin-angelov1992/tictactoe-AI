package courseProject.gamestates;

import coarseProject.Game;
import coarseProject.Position;

public class GameOverState extends GameState {

	public GameOverState(Game game) {
		super(game);
	}

	@Override
	public boolean validMove(Position pos) {
		return false;
	}
}