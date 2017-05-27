package courseProject.movementProviders;

import java.util.List;
import java.util.Random;

import coarseProject.Position;
import courseProject.boards.Board;

public class RandomMovementProvider extends MovementProvider {

	private Random random = new Random();

	@Override
	public Position getAction(Board state) {
		List<Position> actions = state.getAllActions();

		int index = random.nextInt(actions.size());

		return actions.get(index);
	}
}