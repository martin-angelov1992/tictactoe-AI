package coarseProject;

import java.util.List;

public class AlphaBetaMovementProvider {
	public static Position getAction(Board initialState) {
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
			int v = maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			System.out.println("v="+v);
			List<Position> actions = state.getAllActions();
			for (Position successor : actions) {
				Board stateWithAction = state.with(successor);

				if (stateWithAction.getScore() == v) {
					return successor;
				}
			}

			return null;
		}

		public int maxValue(Board state, int alpha, int beta, int depth) {
			if (terminalTest(state) || depth == 0 || suspended) {
				int score = state.getScore();
				state.setValue(score);
				return score;
			}

			int v = Integer.MIN_VALUE;

			List<Position> actions = state.getAllActions();

			for (Position successor : actions) {
				if (!state.validMove(state.getBot(), successor)) {
					System.err.println("Invalid Move!");
					continue;
				}

				Board newState = state.with(successor);
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

		public int minValue(Board state, int alpha, int beta, int depth) {
			if (terminalTest(state) || depth == 0 || suspended) {
				int score = state.getScore();
				return score;
			}

			int v = Integer.MAX_VALUE;

			List<Position> actions = state.getAllActions();

			for (Position successor : actions) {
				if (!state.validMove(state.getPlayer(), successor)) {
					System.err.println("Invalid Move!");
					continue;
				}

				Board newState = state.with(successor);
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

		public static boolean terminalTest(Board state) {
			Player winner = state.getWinner();
			return winner != null || !state.hasMoreMoves();
		}
	}

}
