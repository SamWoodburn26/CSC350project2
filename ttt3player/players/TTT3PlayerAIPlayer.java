package ttt3player.players;

import java.util.ArrayList;

import ttt3player.mvc.TTT3PlayerModel;

public class TTT3PlayerAIPlayer extends TTT3PlayerAbstractPlayer {
	TTT3PlayerModel model;
	char symbol;
	int playerNumber;

	public TTT3PlayerAIPlayer(TTT3PlayerModel model, char symbol) {
		this.model = model;
		this.symbol = symbol;
		switch (symbol) {
			case 'X':
				this.playerNumber = 0;
				break;
			case 'O':
				this.playerNumber = 1;
				break;
			case '+':
				this.playerNumber = 2;
				break;
			default:
				throw new IllegalArgumentException("Invalid player symbol");
		}
	}

	// Assume actions are numbered 1-16
	public char[][] result(char[][] state, int action) {
		// Deep copy the state
		char[][] newstate = new char[4][4];
		for (int row = 0; row < 4; row++)
			for (int col = 0; col < 4; col++)
				newstate[row][col] = state[row][col];

		char turn = this.getTurn(state);

		action -= 1;
		int col = action % 4;
		int row = action / 4;
		newstate[row][col] = turn;

		return newstate;
	}

	public int[] actions(char[][] state) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int row = 0; row < 4; row++)
			for (int col = 0; col < 4; col++)
				if (state[row][col] == '-')
					moves.add(row * 4 + col + 1);

		int[] results = new int[moves.size()];
		for (int i = 0; i < results.length; i++)
			results[i] = moves.get(i);

		return results;
	}

	public boolean terminalTest(char[][] state) {
		// Check for horizontal win
		for (int row = 0; row < 4; row++) {
			for (int startcol = 0; startcol < 2; startcol++) {
				if (state[row][startcol] != '-' && state[row][startcol] == state[row][startcol + 1]
						&& state[row][startcol] == state[row][startcol + 2])
					return true;
			}
		}
		// Check for vertical win
		for (int col = 0; col < 4; col++) {
			for (int startrow = 0; startrow < 2; startrow++) {
				if (state[startrow][col] != '-' && state[startrow][col] == state[startrow + 1][col]
						&& state[startrow][col] == state[startrow + 2][col])
					return true;
			}
		}
		// Check for diagonal \ win
		for (int startrow = 0; startrow < 2; startrow++) {
			for (int startcol = 0; startcol < 2; startcol++) {
				if (state[startrow][startcol] != '-'
						&& state[startrow][startcol] == state[startrow + 1][startcol + 1]
						&& state[startrow][startcol] == state[startrow + 2][startcol + 2])
					return true;
			}
		}
		for (int startrow = 2; startrow < 4; startrow++) {
			for (int startcol = 0; startcol < 2; startcol++) {
				if (state[startrow][startcol] != '-'
						&& state[startrow][startcol] == state[startrow - 1][startcol + 1]
						&& state[startrow][startcol] == state[startrow - 2][startcol + 2])
					return true;
			}
		}

		return isDraw(state);
	}

	public int[] utility(char[][] state) {
		char winningSymbol = getWinner(state);
		if (winningSymbol == symbol) {
			int[] utilResult = { -1000, -1000, -1000 };
			utilResult[playerNumber] = 1000;
			return utilResult;
		} else if (winningSymbol != '-') {
			if (winningSymbol == 'X')
				return new int[] { 1000, -1000, -1000 };
			if (winningSymbol == 'O')
				return new int[] { -1000, 1000, -1000 };
			if (winningSymbol == '+')
				return new int[] { -1000, -1000, 1000 };
		} else if (isDraw(state))
			return new int[] { 0, 0, 0 };

		return new int[] { -1, -1, -1 }; // should not happen
	}

	protected boolean isDraw(char[][] state) {
		boolean allFilled = true;
		for (int row = 0; row < 4; row++)
			for (int col = 0; col < 4; col++)
				if (state[row][col] == '-')
					allFilled = false;
		return allFilled;
	}

	protected char getWinner(char[][] state) {
		// Check for horizontal win
		for (int row = 0; row < 4; row++) {
			for (int startcol = 0; startcol < 2; startcol++) {
				if (state[row][startcol] != '-' && state[row][startcol] == state[row][startcol + 1]
						&& state[row][startcol] == state[row][startcol + 2])
					return state[row][startcol];
			}
		}
		// Check for vertical win
		for (int col = 0; col < 4; col++) {
			for (int startrow = 0; startrow < 2; startrow++) {
				if (state[startrow][col] != '-' && state[startrow][col] == state[startrow + 1][col]
						&& state[startrow][col] == state[startrow + 2][col])
					return state[startrow][col];
			}
		}
		// Check for diagonal \ win
		for (int startrow = 0; startrow < 2; startrow++) {
			for (int startcol = 0; startcol < 2; startcol++) {
				if (state[startrow][startcol] != '-'
						&& state[startrow][startcol] == state[startrow + 1][startcol + 1]
						&& state[startrow][startcol] == state[startrow + 2][startcol + 2])
					return state[startrow][startcol];
			}
		}
		for (int startrow = 2; startrow < 4; startrow++) {
			for (int startcol = 0; startcol < 2; startcol++) {
				if (state[startrow][startcol] != '-'
						&& state[startrow][startcol] == state[startrow - 1][startcol + 1]
						&& state[startrow][startcol] == state[startrow - 2][startcol + 2])
					return state[startrow][startcol];
			}
		}

		return '-'; // Should not happen
	}

	protected char getTurn(char[][] state) {
		int empties = 0;
		for (int row = 0; row < 4; row++)
			for (int col = 0; col < 4; col++)
				if (state[row][col] == '-')
					empties++;

		if (empties % 3 == 1)
			return 'X';
		else if (empties % 3 == 0)
			return 'O';
		else
			return '+';
	}

	public int getMove() {
		char[][] currentState = model.getGrid(); // Get the current state of the board
		int bestMove = -1;
		int bestValue = Integer.MIN_VALUE;

		// Iterate through all possible actions
		for (int action : actions(currentState)) {
			// Get the resulting state after taking the action
			char[][] newState = result(currentState, action);

			// Evaluate the new state using the Minimax function
			int moveValue = minimax(newState, 3, false); // Depth can be adjusted

			// Update the best move if the current move is better
			if (moveValue > bestValue) {
				bestValue = moveValue;
				bestMove = action;
			}
		}

		return bestMove; // Return the best move
	}

	//   Minimax function
	private int minimax(char[][] state, int depth, boolean isMaximizing) {
		// Check if the state is terminal or if the maximum depth is reached
		if (terminalTest(state) || depth == 0) {
			return utility(state)[playerNumber]; // Return the utility for the current AI player
		}

		if (isMaximizing) {
			return maxValue(state, depth); // Call the max function
		} else {
			return minValue(state, depth); // Call the min function
		}
	}

	// maxValue
	private int maxValue(char[][] state, int depth) {
		int v = Integer.MIN_VALUE;

		// Iterate through all possible actions
		for (int action : actions(state)) {
			// Get the resulting state after taking the action
			char[][] newState = result(state, action);

			// Evaluate the new state using the Minimax function (minimizing turn)
			v = Math.max(v, minimax(newState, depth - 1, false));
		}

		return v;
	}

	// minValue
	private int minValue(char[][] state, int depth) {
		int v = Integer.MAX_VALUE;

		// Iterate through all possible actions
		for (int action : actions(state)) {
			// Get the resulting state after taking the action
			char[][] newState = result(state, action);

			// Evaluate the new state using the Minimax function (maximizing turn)
			v = Math.min(v, minimax(newState, depth - 1, true));
		}

		return v;
	}

}