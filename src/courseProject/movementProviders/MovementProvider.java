package courseProject.movementProviders;

import coarseProject.Position;
import courseProject.boards.Board;

public abstract class MovementProvider {

	public abstract Position getAction(Board initialState);

	public static MovementProvider getDefaultInstance() {
		return new AlphaBetaMovementProvider();
	}
}