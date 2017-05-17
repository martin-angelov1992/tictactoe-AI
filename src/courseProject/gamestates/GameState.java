package courseProject.gamestates;

import coarseProject.Player;
import coarseProject.Position;

public interface GameState {
	public boolean validMove(Player player, Position pos);
}
