package courseProject.movementProviders;

import coarseProject.Position;
import courseProject.boards.Board;

public interface MovementProvider {

	Position getAction(Board initialState);

}
