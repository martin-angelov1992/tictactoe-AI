package courseProject.gamestates;

import coarseProject.Game;
import coarseProject.Position;

public class PlayerTurnState extends GameState {

	public PlayerTurnState(Game game) {
		super(game);
	}

	@Override
	public boolean validMove(Position pos) {
		// pass the rest of the validation to the board
		return game.getBoard().validMove(game.getPlayer(), pos);	
	}
}