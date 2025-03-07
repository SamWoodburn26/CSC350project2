package c4.players;

import java.util.Random;

import c4.mvc.ConnectFourModelInterface;

public class ConnectFourAIPlayer extends ConnectFourPlayer{
    ConnectFourModelInterface model;
	Random random;
	
    //question 1- connect four ai player
	public ConnectFourAIPlayer(ConnectFourModelInterface model){
		this.model = model;
		this.random = new Random();
	}

    //question 2- terminal test
    public boolean terminalTest(int[][] state){
        model.setGrid(state);
        //check for winner or draw
        if(model.checkForWinner() >= 0 || model.checkForDraw()){
            return true;
        }
        //otherwise return false
        else{
            return false;
        }
	}

    //question 3- actions
    public int[] actions(int[][] state){
        boolean[] validMovesBool = model.getValidMoves();
        //go through valid moves boolean and covert to which int columns are free
        int size = 0;
        for(int i = 0; i<validMovesBool.length; i++){
            if(validMovesBool[i]){
                size++;
            }
        }
        int[] validMovesInt = new int[size];
        int arrIndex = 0;
        for(int i = 0; i<validMovesBool.length; i++){
            if(validMovesBool[i]){
                validMovesInt[arrIndex] = i;
                arrIndex++;
            }
        }
        return validMovesInt;
    }

    //question 4: results
    public int[][] result(int[][] state, int action){
        model.setGrid(state);
        model.setGridPosition(action, model.getTurn());
        return model.getGrid();
    }

    //question 5: alpha beta pruning 
    @Override
	public int getMove() {
		return alphaBetaSearch(model.getGrid());
	}

    public int utility(int[][] state){
        model.setGrid(state);
        if(model.checkForWinner() >= 0){
            return 1000;
        }
        else if(model.checkForDraw()){
            return 0;
        }
        else{
            return -1000;
        }
    }

    //returns an action (int)
    public int alphaBetaSearch(int[][] state){
        int bestAction = -1;
    	double bestValue = Double.NEGATIVE_INFINITY;
    	int[] availableActions = actions(state);

    	for (int action : availableActions) {
        	int[][] newState = result(state, action);
        	int v = minValue(newState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); // Start with minValue for opponent's turn
        	if (v > bestValue) {
            	bestValue = v;
            	bestAction = action;
        	}
    	}
    	return bestAction;
	}
	
	public int maxValue(int[][] state, double alpha, double beta){
		//if current state is terminal state (end of game) then return the utility
        if(terminalTest(state)){
            return utility(state);
        }
        double v = Double.NEGATIVE_INFINITY;
        //for action in actions
        for(int action: actions(state)){
            //for each a in action state: for each column 0-6
            //v = the higher value between v and min value (max v and min)
            v = Math.max(v, minValue(result(state, action), alpha, beta));
            //if v's greater than or equal to beta return v
            if(v >= beta){
                return (int)v;
            }
            //alpha = higher value between alpha and v (max alpha and v)
            alpha = Math.max(alpha, v);
        }
        //return v
		return (int)v;
	}
	
	public int minValue(int[][] state, double alpha, double beta){
		//if current state is terminal state (end of game) then return the utility
        if(terminalTest(state)){
            return utility(state);
        }
        double v = Double.POSITIVE_INFINITY;
        //for each column in state
        for(int action: actions(state)){
            //for each a in action state: for each column 0-6
            //v = the lower value between v and max value (min v and min)
            v = Math.min(v, maxValue(result(state, action), alpha, beta));
            //if v's less than or equal to beta return v
            if(v <= alpha){
                return (int)v;
            }
            //beta = lower value between beta and v (min beta and v)
            beta = Math.min(beta, v);
        }
        //return v
		return (int)v;
	}
}
