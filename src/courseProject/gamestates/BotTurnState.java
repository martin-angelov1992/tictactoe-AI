package courseProject.gamestates;

import coarseProject.Game;
import coarseProject.Position;

public class BotTurnState extends GameState {

	public BotTurnState(Game game) {
		super(game);
	}

	@Override
	public boolean validMove(Position pos) {
		return false;
	}
}