package c4.players;

import java.util.ArrayList;

import c4.mvc.ConnectFourModel;
import c4.mvc.ConnectFourModelInterface;

public class ConnectFourAIPlayer extends ConnectFourPlayer {
    ConnectFourModelInterface model;
    int cutoff;

    //QUESTION 1: CONNECT FOUR AI PLAYER
    /* constructor, set model */
    public ConnectFourAIPlayer(ConnectFourModelInterface model) {
        this.model = model;
        this.cutoff = 42; //default max depth (size of connect four board)
    }



    //QUESTION 6: DIFFICULTY
    /* constructor to set model and a depth cutoff */
    public ConnectFourAIPlayer(ConnectFourModelInterface model, int cutoff) {
        this.model = model;
        this.cutoff = cutoff;
    }



    //QUESTION 2: TERMINAL TEST
    /* int[][] parameter, returns a boolean */
    public boolean terminalTest(int[][] state) {
        //new temp model with its grid being state parameter
        ConnectFourModel tempModel = new ConnectFourModel();
        //reset grid and turn
        tempModel.initialize(); 
        //make a deep copy of the state
        tempModel.grid = deepCopy(state); 
        //check for winner or draw in that new state
        return tempModel.checkForWinner() >= 0 || tempModel.checkForDraw();
    }
    
    /* helper method for terminal test & results: take an int[][] parameter, make a copy of that parameter, return the copy */
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }



    //QUESTION3: ACTIONS
    /* int[][] parameter, returns int[] of valid moves 0-6 */
    public int[] actions(int[][] state) {
        // array list of valid moves
        ArrayList<Integer> validMoves = new ArrayList<>();
        //loop through each column
        for (int col = 0; col < 7; col++) {
            //top row empty means that it is available, so add it to the arraylist
            if (state[col][0] == -1) { 
                validMoves.add(col);
            }
        }
        //convert arraylist to int[]
        int[] result = new int[validMoves.size()];
        for (int i = 0; i < validMoves.size(); i++) {
            result[i] = validMoves.get(i);
        } 
        //return int[]
        return result;
    }



    //QUESTION 4: RESULTS
    /* takes parameter state and action, returns int[][] of the resulting state after the action */
    public int[][] result(int[][] state, int action) {
        //action is already a column index (0-6)
        int col = action; 
        //row is the lowest empty index in that column
        int row = findLowestEmptyIndex(state, col);
        //if row = -1 then the column is full
        if (row == -1) {
            throw new IllegalArgumentException("Column " + col + " is full.");
        }
        //using helper method, make a deep copy of state
        int[][] newState = deepCopy(state);

        /* print valid moves in actions for debugging */
        // System.out.print("Valid moves after move in col " + col + ": ");
        // for(int a: actions(newState)){
        //     System.out.print(a + ", ");
        // }

        //get the turn from the given state
        int player = getTurn(state);
        //use the players turn to set the correct token in the previously determined (row, col)
        newState[col][row] = player;
        //return the new resulting state
        return newState;
    }

    /* helper method for results: finds the lowest empty spot in the column, so the token would 'fall' to that spot */
    private int findLowestEmptyIndex(int[][] state, int col) {
        //loop from bottom of column to top
        for (int i = state[col].length - 1; i >= 0; i--) {
            //return first empty spot (-1)
            if (state[col][i] == -1) {
                return i;
            }
        }
        //return -1 if column is full
        return -1; 
    }

    /* helper method for results: takes in a state parameter and returns that states current turn */
    private int getTurn(int[][] state) {
        int filled = 0;
        //find number of moves made so far (filled spaces)
        for (int c = 0; c < 7; c++) {
            for (int r = 0; r < 6; r++) {
                if (state[c][r] != -1) filled++;
            }
        }
         //player 1 starts, so even moves = player 1’s turn
         if (filled % 2 == 0) {
            return 1;
        } else {
            return 2;
        }
        
    }



    //QUESTION 7: UTILITY EXPLANATION
        /*
         * for this utility function we decide to use two factors to determine the best possible action.
         *  • the first is how good the column is:
         *      - we assigned a weight value to each column (0-2)
         *      - an int array was created to hold these weight values, so we could easily retrieve 
         *        a columns weight based on its index
         *      - the center columns were given higher values, while the outer columns had lower 
         *        weight values
         *      - therefore, the weight array looks like this: {0,0,1,2,1.0,0}
         *      - reasoning: when placing a token in  the center, you have the possilibily of getting 
         *        a four in a row in any of the 5 directions (vertical, hoirzontal moving left, 
         *        horizontal moving right, positive diagnol, and negative diagnol). when placing a 
         *        token in the outer columns, your options become more limited.  instead of being able 
         *        to try for a four in a row in every direction, you can only try for 3 directions 
         *        (vertical, hoirzontal moving left OR horizontal moving right (depending on which side 
         *        of the board the token is placed), and positive diagnol OR negative diagnol (depending
         *        on which side of the board the token is placed). therfore, when placing a token you 
         *        are better off if it is in the center rather than closer to the edge because you have 
         *        more opportunities to get a four in a row.
         *  • the second is the possibility for there to be a three in a row in either the vertical or 
         *    horizontal direction:
         *      - used a seperate function to make the code cleaner
         *      - have a count varibale (intialized to 0) to count the number of potential vertical and 
         *        horizontal three in a rows
         *      - for vertical checks: this function loops through each column and set of three rows, then
         *        keeps track of the number of spaces occupied by the player (given by the parameter) and
         *        and the number of empty spaces, if in that iteration of the loop there are 3 player
         *        spaces and 1 empty space then increase count by one
         *      - for horizontal checks: this function loops through each row and set of three columns, 
         *        then keeps track of the number of spaces occupied by the player (given by the parameter)
         *        and the number of empty spaces, if in that iteration of the loop there are 3 player
         *        spaces and 1 empty space then increase count by one
         *      - lastly, return the count variable
         *      - reasoning: we wanted this to be the second condition checked in the utility function because
         *        the goal of connect four is to get four in a row, so logically the best way to make your
         *        decision on which move to make next would be to see where you'll have the best chance of
         *        getting to that four in a row, and that would be any place where you can havethree in a row
         *  • how it works: 
         *      - first we made a variable to represent the 'goodness' of an action, which is initially
         *        set to 0
         *      - we create a copy the state in a new temporary connect four model in order to calculate
         *        for the given state
         *      - handle the terminal states of winning, losing, or a draw; we kept the previous values 
         *        returning 1000 for win, -1000 for loss, and 0 for draw
         *      - then handle the non-terminal states (every other position in game play that does not end
         *        the game); this is where the goodness is used
         *      - loop through each spot in the board, if it is occupied by the ai player then add the weight 
         *        of that column to the goodness score, if it is occupied by the opponent then subtract the
         *        weight of that column from the overall goodness score
         *      - after that we calculated the potential threes for the ai player and add that * 50 to the
         *        overall goodness and calculated the potential threes for the opponent and subtracted
         *        that * 50 from the overall goodness 
         *      - finally, after all calculations are done, return the goodness
         */

    /* utility- determine the 'goodness' of each possible action for given state, returns int goodness */
    public int utility(int[][] state) {
        //make a temporary model to hold a copy of the given state
        ConnectFourModel tempModel = new ConnectFourModel();
        tempModel.initialize();
        tempModel.grid = deepCopy(state);
        //check for winner and get current turn
        int winner = tempModel.checkForWinner();
        int aiPlayer = model.getTurn();
    
        //terminal states (winner and draw)
        if (winner >= 0) {
        if (winner == aiPlayer) {
            return 1000;
        } else {
            return -1000;
        }
        } else if (tempModel.checkForDraw()) {
            return 0;
        }

    
        //non-terminal evaluation (calculate goodness score)
        int goodness = 0;
        int opponent;
        if (aiPlayer == 1) {
            opponent = 2;
        } else {
            opponent = 1;
        }

        //more important centers (assign a weight to each column: columns 2, 3, 4 are more valuable)
        int[] centerWeights = {0, 0, 1, 2, 1, 0, 0}; 
        //loop through the board
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                //if current spot is the ai player add then the weight value of that column to goodness score
                if (state[col][row] == aiPlayer) { 
                    goodness += centerWeights[col];
                } 
                //if current spot is the opponent then subtract the weight value of that column from goodness score
                else if (state[col][row] == opponent) { 
                    goodness -= centerWeights[col];
                }
            }
        }
        //calculate the potential three-in-a-rows (heuristic), add to goodness score if ai player, subtract from goodness score if opponent
        goodness += countPotentialThrees(state, aiPlayer) * 50;
        goodness -= countPotentialThrees(state, opponent) * 50;
        //return the goodness score
        return goodness;
    }
    
    /* helper method for utility: calculate the number of potential three in a rows */
    private int countPotentialThrees(int[][] state, int player) {
        int count = 0;
        /* horizontal  potential three in a rows*/
        //loop through each row and sets of 3 columns
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                //keep track of the current players position and number of empty positions
                int playerCount = 0;
                int emptyCount = 0;
                for (int i = 0; i < 4; i++) {
                    if (state[col + i][row] == player){
                        playerCount++;
                    } else if(state[col + i][row] == -1) {
                        emptyCount++;
                    }
                }
                //if player has 3 and 1 empty then increase count
                if (playerCount == 3 && emptyCount == 1) count++;
            }
        }
        /* vertical potential three in a rows */
         //loop through each column and sets of 3 rows
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                //keep track of the current players position and number of empty positions
                int playerCount = 0;
                int emptyCount = 0;
                for (int i = 0; i < 4; i++) {
                    if (state[col][row + i] == player) {
                        playerCount++; 
                    }
                    else if (state[col][row + i] == -1) {
                        emptyCount++; 
                    }
                }
                //if player has 3 and 1 empty then increase count
                if (playerCount == 3 && emptyCount == 1) count++;
            }
        }
        /* add diagonal checks for an even better ai player (did not have time to implement)*/
        //return count
        return count;
    }



    //QUESTION 5: ALPHA BETA PRUNING
    /* get move- override connect four players get move to run the alpha beta search for the ai players move */
    @Override
    public int getMove() {
        return alphaBetaSearch(model.getGrid());
    }


    /* alpha beta search- takes an int[][] state parameter and returns an int- the best action for the given state */
    public int alphaBetaSearch(int[][] state) {
        //get the available actions
        int[] availableActions = actions(state);
        int bestAction = -1;
        double bestValue;
        //ai player is the root
        int aiPlayer = model.getTurn(); // AI’s identity at root
        int currentPlayer = getTurn(state);
        if (aiPlayer == currentPlayer) {
            //if ai is the current player you want to maximize, best value is negative infinity
            bestValue = Double.NEGATIVE_INFINITY;
            //for each action in actions
            for (int action : availableActions) {
                int[][] newState = result(state, action);
                //get the minValue in the new resulting state after performing the action from negative infinity to positive infinity
                int v = minValue(newState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1); //starting depth 1
                //if greater than the current best value (maximizing), change best value to v and best action to the current action
                if (v > bestValue) {
                    bestValue = v;
                    bestAction = action;
                }
            }
        } else {
            //if ai is player 2 you want to minimize first
            bestValue = Double.POSITIVE_INFINITY;
            //for each action in actions
            for (int action : availableActions) {
                int[][] newState = result(state, action);
                //get the maxValue in the new resulting state after performing the action from negative infinity to positive infinity
                int v = maxValue(newState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1); //starting depth 1
                //if greater than the current best value (minimizing), change best value to v and best action to the current action
                if (v < bestValue) {
                    bestValue = v;
                    bestAction = action;
                }
            }
        }
        //return the best action!
        return bestAction;
    }

    /* max value mini-max */
    public int maxValue(int[][] state, double alpha, double beta, int depth) {
        // System.out.println("max called");
        // if current state is terminal state (end of game) then return the utility
        if (terminalTest(state) || depth >= cutoff) {
            return utility(state);
        }
        double v = Double.NEGATIVE_INFINITY;
        // for action in actions
        for (int action : actions(state)) {
            // for each a in action state: for each column 0-6
            // v = the higher value between v and min value (max v and min)
            v = Math.max(v, minValue(result(state, action), alpha, beta,depth+1));
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

     /* min value mini-max */
    public int minValue(int[][] state, double alpha, double beta, int depth) {
        // System.out.println("min called");
        // if current state is terminal state (end of game) then return the utility
        if (terminalTest(state) || depth >= cutoff) {
            return utility(state);
        }
        double v = Double.POSITIVE_INFINITY;
        // for each column in state
        for (int action : actions(state)) {
            // for each a in action state: for each column 0-6
            // v = the lower value between v and max value (min v and min)
            v = Math.min(v, maxValue(result(state, action), alpha, beta, depth+1));
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
