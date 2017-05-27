package courseProject.gamestates;

import coarseProject.Game;
import coarseProject.Position;

public abstract class GameState {
	protected Game game;

	public GameState(Game game) {
		this.game = game;
	}

	public abstract boolean validMove(Position pos);
}