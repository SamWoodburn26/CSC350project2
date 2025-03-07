package c4.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	
	@Override
	public int getMove() {
		boolean[] moves = model.getValidMoves();
        //find the first valid move and return it
        int num = 0;
        for(int i = 0; i < moves.length; i++){
            if (moves[i] == true){
                num = i;
            }
        }
        return num;
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
        boolean[] validMovesBool = new boolean[7];
        int[] currCol = new int[state[0].length];
        int usedSpacesInCol = 0;
        //first set all booleans to true
        for(int i = 0; i<7; i++){
            validMovesBool[i] = true;
        }
        //loop through the current state of the board and check if the row is full
        for(int col = 0; col<state.length; col++){
            for(int row=0; row< state[0].length; row++){
                currCol[row] = state[col][row];
                //if the whole row is full mark that spot in the bool array as false
			}
            for(int i = 0; i< currCol.length; i++){
                //if the spot is not empty increase the numbe of spots filled in that column
                if(currCol[i] != -1){
                    usedSpacesInCol++;
                }
            }
            //if the number of used spaces the columns length then the column is full
            if(usedSpacesInCol == currCol.length){
                validMovesBool[col] = false;
            }
        }

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
}
