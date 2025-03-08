package tictactoe.players;

import java.util.ArrayList;

import tictactoe.mvc.TicTacToeModel;

public class TicTacToeAIPlayer extends TicTacToePlayer {
	TicTacToeModel model;
	char symbol;
	
	public TicTacToeAIPlayer(TicTacToeModel model, char symbol){
		this.model = model;
		this.symbol = symbol;
	}
	
	// Assume actions are numbered 1-9
	public char[][] result(char[][] state, int action){
		// Deep copy the state
		char[][] newstate = new char[3][3];
		for(int row=0; row<3; row++)
			for(int col=0; col<3; col++)
				newstate[row][col] = state[row][col];
		
		char turn = this.getTurn(state);
		
		action -= 1;
		int col = action % 3;
		int row = action / 3;
		newstate[row][col] = turn;
		
		return newstate;
	}
	
	public int[] actions(char[][] state){
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for(int row=0; row<3; row++)
			for(int col=0; col<3; col++)
				if(state[row][col] == '-')
					moves.add(row*3+col+1);
		
		int[] results = new int[moves.size()];
		for(int i=0; i<results.length; i++)
			results[i] = moves.get(i);
		
		return results;
	}
	
	public boolean terminalTest(char[][] state){
		for(int row=0; row<3; row++){
			if(state[row][0] != '-' && state[row][0] == state[row][1] && state[row][0] == state[row][2])
				return true;
		}
		for(int col=0; col<3; col++){
			if(state[0][col] != '-' && state[0][col] == state[1][col] && state[0][col] == state[2][col])
				return true;
		}
		if(state[0][0] != '-' && state[0][0] == state[1][1] && state[0][0] == state[2][2])
				return true;
		if(state[2][0] != '-' && state[2][0] == state[1][1] && state[2][0] == state[0][2])
				return true;
		
		return isDraw(state);
	}
	
	public int utility(char[][] state){
		if(getWinner(state) == symbol)
			return 1000;
		else if(getWinner(state) != '-')
			return -1000;
		else if(isDraw(state))
			return 0;
		
		return 0; //should not happen
	}
	
	protected boolean isDraw(char[][] state){
		boolean allFilled = true;
		for(int row=0; row<3; row++)
			for(int col=0; col<3; col++)
				if(state[row][col] == '-')
					allFilled = false;
		return allFilled;
	}
	
	protected char getWinner(char[][] state){
		for(int row=0; row<3; row++){
			if(state[row][0] != '-' && state[row][0] == state[row][1] && state[row][0] == state[row][2])
				return state[row][0];
		}
		for(int col=0; col<3; col++){
			if(state[0][col] != '-' && state[0][col] == state[1][col] && state[0][col] == state[2][col])
				return state[0][col];
		}
		if(state[0][0] != '-' && state[0][0] == state[1][1] && state[0][0] == state[2][2])
				return state[0][0];
		if(state[2][0] != '-' && state[2][0] == state[1][1] && state[2][0] == state[0][2])
				return state[2][0];
		
		return '-'; // Should not happen
	}
	
	protected char getTurn(char[][] state){
		int empties = 0;
		for(int row=0; row<3; row++)
			for(int col=0; col<3; col++)
				if(state[row][col] == '-')
					empties++;
		
		if(empties%2 == 1)
			return 'X';
		else
			return 'O';
	}
	
	public int getMove(){
		/* REPLACE WITH YOUR CODE */
		return alphaBetaSearch(model.getGrid());
    }

    	public int utility(int[][] state) {
        	TicTacToeModel tempModel = new TicTacToeModel();
        	// tempModel.setGrid(state);
        	if (tempModel.checkForWinner() >= 0) {
            	return 1000;
        	} else if (tempModel.checkForDraw()) {
            		return 0;
        	} else {
            		return -1000;
        	}
    }
	
	public int alphaBetaSearch(char[][] state){
		/* REPLACE WITH YOUR CODE */
		int bestAction = -1;
        	double bestValue = Double.NEGATIVE_INFINITY;
        	int[] availableActions = actions(state);
        	System.out.println("RAN ");
        	// System.out.println("search called");
        	for (int action : availableActions) {
            	char[][] newState = result(state, action);
            	int v = minValue(newState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); // Start with minValue for
                                                                                            // opponent's turn
            if (v > bestValue) {
                bestValue = v;
                bestAction = action;
            }
        }
        System.out.println("wow");
        return bestAction;
	}
	
	public int maxValue(char[][] state, double alpha, double beta){
		/* REPLACE WITH YOUR CODE */
		// if current state is terminal state (end of game) then return the utility
		if (terminalTest(state)) {
			return utility(state);
		    }
		    double v = Double.NEGATIVE_INFINITY;
		    // for action in actions
		    for (int action : actions(state)) {
			// for each a in action state: for each column 0-6
			// v = the higher value between v and min value (max v and min)
			v = Math.max(v, minValue(result(state, action), alpha, beta));
			// if v's greater than or equal to beta return v
			if (v >= beta) {
			    return (int) v;
			}
			// alpha = higher value between alpha and v (max alpha and v)
			alpha = Math.max(alpha, v);
		    }
		    // return v
		    return (int) v;
	}
	
	public int minValue(char[][] state, double alpha, double beta){
		/* REPLACE WITH YOUR CODE */
		// if current state is terminal state (end of game) then return the utility
		if (terminalTest(state)) {
			return utility(state);
		    }
		    double v = Double.POSITIVE_INFINITY;
		    // for each column in state
		    for (int action : actions(state)) {
			// for each a in action state: for each column 0-6
			// v = the lower value between v and max value (min v and min)
			v = Math.min(v, maxValue(result(state, action), alpha, beta));
			// if v's less than or equal to beta return v
			if (v <= alpha) {
			    return (int) v;
			}
			// beta = lower value between beta and v (min beta and v)
			beta = Math.min(beta, v);
		    }
		    // return v
		    return (int) v;
	}
}
