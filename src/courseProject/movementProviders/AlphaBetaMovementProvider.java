package courseProject.movementProviders;

import java.util.List;

import coarseProject.Player;
import coarseProject.Position;
import courseProject.boards.AIBoard;
import courseProject.boards.Board;

public class AlphaBetaMovementProvider extends MovementProvider {
	public Position getAction(Board initialState) {
		Thinker thinker = new Thinker();
		thinker.setInitialState(initialState);
		thinker.start();
		try {
			Thread.sleep(Board.MOVE_SECONDS*1000);
		} catch (Exception e) {
			System.err.println(e);
		}
		thinker.setSuspended();
		return thinker.getAction();
	}

	private static class Thinker extends Thread {
		private volatile Position action;
		private volatile Board initialState;
		private volatile boolean suspended;

		@Override
		public void run() {
			for (int i = 1; i <= 10; ++i) {
				if (suspended) {
					break;
				}

				Position newAction = alphaBetaSearch(initialState, i);
				if (newAction != null) {
					action = newAction;
				}
			}
		}

		public void setSuspended() {
			suspended = true;
		}

		public void setInitialState(Board initialState) {
			this.initialState = initialState;
		}

		public Position getAction() {
			return action;
		}

		public Position alphaBetaSearch(Board state, int depth) {
			AIBoard aiBoard = new AIBoard(state);
			int v = maxValue(aiBoard, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			List<Position> actions = state.getAllActions();
			for (Position successor : actions) {
				AIBoard stateWithAction = aiBoard.with(successor);

				if (stateWithAction.getScore() == v) {
					return successor;
				}
			}

			return null;
		}

		public int maxValue(AIBoard state, int alpha, int beta, int depth) {
			if (terminalTest(state) || depth == 0 || suspended) {
				int score = state.getScore();
				state.setValue(score);
				return score;
			}

			int v = Integer.MIN_VALUE;

			List<Position> actions = state.getBoard().getAllActions();

			for (Position successor : actions) {
				if (!state.getBoard().validMove(state.getBoard().getBot(), successor)) {
					System.err.println("Invalid Move!");
					continue;
				}

				AIBoard newState = state.with(successor);
				int minValue = minValue(newState, alpha, beta, depth - 1);
				v = Math.max(v, minValue);
				newState.setValue(v);
				alpha = Math.max(alpha, v);

				if (alpha >= beta) {
					return v;
				}
			}

			state.setValue(v);
			return v;
		}

		public int minValue(AIBoard state, int alpha, int beta, int depth) {
			if (terminalTest(state) || depth == 0 || suspended) {
				int score = state.getScore();
				return score;
			}

			int v = Integer.MAX_VALUE;

			List<Position> actions = state.getBoard().getAllActions();

			for (Position successor : actions) {
				if (!state.getBoard().validMove(state.getBoard().getPlayer(), successor)) {
					System.err.println("Invalid Move!");
					continue;
				}

				AIBoard newState = state.with(successor);
				int maxValue = maxValue(newState, alpha, beta, depth - 1);
				v = Math.min(v, maxValue);
				newState.setValue(v);
				beta = Math.min(beta, v);

				if (beta <= alpha) {
					newState.setValue(v);
					return v;
				}
			}

			state.setValue(v);
			return v;
		}

		public static boolean terminalTest(AIBoard state) {
			Player winner = state.getBoard().getWinner();
			return winner != null || !state.getBoard().hasMoreMoves();
		}
	}
}