package three_stones.views;

//import ThreeStones_Game.enums.Turn;
import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a Board and all its components needed to play
 * the 3 stones game. A Board is composed of a gameBoard, a two dimensional array
 * of 11 by 11. The board is made to be 11 by 11 to not run into an 
 * IndexOutOfBounds exception. Each slot in the array is represented by a number
 * that will dictate the state each slot is in. 0 represents a slot where either
 * the server or the client can make a move in. 1 represents a slot where the client
 * has already made a move in. 2 represents a slot where the server has made a 
 * move in. 9 represents a space where making a move in is impossible. The board
 * class also keeps track of the player score, the server score and the last slot
 * where a move was made in.
 *
 * @author Sebastian Ramirez
 * @author Derek McLean
 */

public class Board{
//    private Turn turn;
    private int[] lastPlayed;
    private final int stoneCount;
    private int[][] gameBoard;
    private String lastPlayedStr = "";
    private int playerScore;    
    private int serverScore;
    private int player;
/**
 * No parameters, default constructor. It initializes a 2 dimensional array representing the
 * game board, the number of stones left to play and initializes the array that will
 * hold the last played move.
 */
    public Board() {
        int[][] gameboard = {
        {9,9,9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9,9,9},
        {9,9,9,9,0,0,0,9,9,9,9},
        {9,9,9,0,0,0,0,0,9,9,9},
        {9,9,0,0,0,0,0,0,0,9,9},
        {9,9,0,0,0,9,0,0,0,9,9},
        {9,9,0,0,0,0,0,0,0,9,9},
        {9,9,9,0,0,0,0,0,9,9,9},
        {9,9,9,9,0,0,0,9,9,9,9},
        {9,9,9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9,9,9}
        };
//        int[][] gameboard = {
//        {9,9,9,9,9,9,9,9,9,9,9},
//        {9,9,9,9,9,9,9,9,9,9,9},
//        {9,9,9,9,1,2,1,9,9,9,9},
//        {9,9,9,2,1,2,1,2,9,9,9},
//        {9,9,1,2,1,2,1,2,1,9,9},
//        {9,9,2,1,2,9,1,2,1,9,9},
//        {9,9,2,1,2,1,2,1,2,9,9},
//        {9,9,9,1,2,0,0,0,9,9,9},
//        {9,9,9,9,0,0,0,9,9,9,9},
//        {9,9,9,9,9,9,9,9,9,9,9},
//        {9,9,9,9,9,9,9,9,9,9,9}
//        };//for testing purposes
        this.gameBoard = gameboard;
        this.stoneCount = 30;
        this.lastPlayed = new int[2];
    }
    /**
     * Gets all the slots available to play after either the client or the server
     * have made a move. This method is expected to return all the empty spaces
     * in the column and the row where the last move was played.
     * @param lastPlayed
     * @return A list containing all the available slots where either the client
     * or the server can make a move in.
     */
    public List<int[]> getAvailable(int[] lastPlayed){
        System.out.println("Last Played: "+lastPlayed[0]+" "+lastPlayed[1]);
        int x = lastPlayed[0];
        int y = lastPlayed[1];
        List<int[]> available = new ArrayList<>();
        for(int i=0;i<11;i++){
            if (this.gameBoard[i][y]==0){
                int[] slot = {i,y};
                System.out.println("Row values: "+slot[0]+" "+slot[1]);
                if(slot[0]!=lastPlayed[0]){
                    available.add(slot);
                }
            }
        }
        for(int j=0;j<11;j++){
            if (this.gameBoard[x][j]==0){
                int[] slot = {x,j};
                System.out.println("Column values: "+slot[0]+" "+slot[1]);
                if(slot[1]!=lastPlayed[1]){
                    available.add(slot);
                }
            }
        }
        return available;
    }
    /**
     * The toString method of the Board class has been overridden in order to
     * represent the actual state of the gameBoard throughout the game between
     * the server and the client.
     * @return A string representation of the Board.
     */
    @Override
    public String toString(){
        //TODO: make it so 9s dont show, and 1 become W, and 2 becomes B
        String str = "\n";
        
        for(int i = 0; i < 11; i++){
            str += "\n" + (i); //vertical index
            if(i < 10){str += " ";} //fixes alignment issues
            for(int j = 0; j < 11; j++){
                if(gameBoard[i][j] == 9) {
                    str += "  "; 
                } else {
                str += " " + gameBoard[i][j]; 
                }
            }   
        }
        str +="\n  ";
        for(int j = 0; j < 11; j++){
                if (j < 11) {
                    str += " ";
                }
                str +=(j); //horizontal index
            }
        
        //display last move
        str += "\n" + lastPlayedStr + "\n";
        return str;
    }
    
    /**
     * The updateBoard method is used to update all the variables in the Board
     * class after a move has been made. It updates the current player making a
     * move as well as the last played slot and the current score for both players.
     * @param player
     * @param position
     */
    public void updateBoard(int player, int[] position){
        this.player = player;
        gameBoard[position[1]][position[0]] = player;
        lastPlayed[0] = position[0];
        lastPlayed[1] = position[1];
        setScore();
        lastPlayedStr = "Last played move: " + position[0] + ", "+ position[1];
        System.out.println("Score:\rPlayer: "+playerScore+" Server: "+serverScore);

    }
    /**
     * Helper method utilized to clear the game board after either the client
     * decides to start a new game or the game is finished to completion.
     */
    public void reset(){
        int [][] gameboard = {
        {9,9,9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9,9,9},
        {9,9,9,9,0,0,0,9,9,9,9},
        {9,9,9,0,0,0,0,0,9,9,9},
        {9,9,0,0,0,0,0,0,0,9,9},
        {9,9,0,0,0,9,0,0,0,9,9}, 
        {9,9,0,0,0,0,0,0,0,9,9},
        {9,9,9,0,0,0,0,0,9,9,9},
        {9,9,9,9,0,0,0,9,9,9,9},
        {9,9,9,9,9,9,9,9,9,9,9},
        {9,9,9,9,9,9,9,9,9,9,9}
        };
        
        gameBoard = gameboard;
        lastPlayedStr = "";
        lastPlayed = new int[2];    
        playerScore = 0;
        serverScore = 0;
        
    }
    /**
     * Gets all the slots available to play after either the client or the server
     * have made a move. This method is expected to return all the empty spaces
     * in the column and the row where the last move was played.
     * @return A list containing all the available slots where either the client
     * or the server can make a move in.
     */
    public List<int[]> getAvailableMoves(){
        List<int[]> moves = new ArrayList<>();
        
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                if(gameBoard[i][j] == 0){
                    moves.add(new int[]{j,i});
                }
            }
        }
        return moves;
    }
    /**
     * Returns the last played slot.
     * @return The slot where the last played move was in.
     */
    public int[] getLastPlayed(){
        return lastPlayed;
    }
    /**
     * This method is called whenever the game board is update. It checks which
     * player just played and sets the score according to it.
     */
    public void setScore(){
        int x = lastPlayed[1];
        int y = lastPlayed[0];
        switch(player){
            case 1:
                playerScore += checkScore(x,y);
                break;
            case 2:
                serverScore += checkScore(x,y);
                break;
            default:
                break;
        }
//        printScore();
    }
    /**
     * This method encompasses the four different check methods needed to 
     * accurately assign points to each player after a move has been played.
     * @param x
     * @param y
     * @return An int representing the total points a player has scored after
     * a move has been played.
     */
        public int checkScore(int x, int y){ //reverse x and y
        int totalMoveScore = 0;
        totalMoveScore += checkDiagonalLR(x,y);
        totalMoveScore += checkDiagonalRL(x,y);
        totalMoveScore += checkVertical(x,y);
        totalMoveScore += checkHorizontal(x,y);
        return totalMoveScore;
    }
    /**
     * This method checks for points in a diagonal that goes left to right.
     * @param x
     * @param y
     * @return An int representing the total points a player has scored in a 
     * left to right diagonal.
     */
    private int checkDiagonalLR(int x, int y){
        int currentScore = 0;
        if(gameBoard[x-2][y-2]==player&&gameBoard[x-1][y-1]==player){
            currentScore++;
        }
        if(gameBoard[x-1][y-1]==player&&gameBoard[x+1][y+1]==player){
            currentScore++;
        }
        if(gameBoard[x+1][y+1]==player&&gameBoard[x+2][y+2]==player){
            currentScore++;
        }
        return currentScore;
    }
        /**
     * This method checks for points in a diagonal that goes right to left.
     * @param x
     * @param y
     * @return An int representing the total points a player has scored in a 
     * right to left diagonal.
     */
    private int checkDiagonalRL(int x, int y){
        int currentScore = 0;
        if(gameBoard[x+2][y-2]==player&&gameBoard[x+1][y-1]==player){
            currentScore++;
        }
        if(gameBoard[x+1][y-1]==player&&gameBoard[x-1][y+1]==player){
            currentScore++;
        }
        if(gameBoard[x-1][y+1]==player&&gameBoard[x-2][y+2]==player){
            currentScore++;
        }
        return currentScore;
    }
    /**
     * This method checks for points in a vertical line.
     * @param x
     * @param y
     * @return An int representing the total points a player has scored in a 
     * vertical line.
     */
    private int checkVertical(int x, int y){
        int currentScore = 0;
        if(gameBoard[x][y+2]==player&&gameBoard[x][y+1]==player){
            currentScore++;
        }
        if(gameBoard[x][y+1]==player&&gameBoard[x][y-1]==player){
            currentScore++;
        }
        if(gameBoard[x][y-1]==player&&gameBoard[x][y-2]==player){
            currentScore++;
        }
        return currentScore;
    }
        /**
     * This method checks for points in an horizontal line.
     * @param x
     * @param y
     * @return An int representing the total points a player has scored in an 
     * horizontal line.
     */
    private int checkHorizontal(int x, int y){
        int currentScore = 0;
        if(gameBoard[x-2][y]==player&&gameBoard[x-1][y]==player){
            currentScore++;
        }
        if(gameBoard[x-1][y]==player&&gameBoard[x+1][y]==player){
            currentScore++;
        }
        if(gameBoard[x+1][y]==player&&gameBoard[x+2][y]==player){
            currentScore++;
        }
        return currentScore;
    }
    /**
     * This method prints the respective scores to the screen.
     */
    public void printScore(){
        System.out.println("Score:\rPlayer: "+playerScore+" Server: "+serverScore);
    }
    /**
     * This method returns the points the server has currently attained.
     * @return The current score of the server. 
     */
    public int getServerScore(){
        return serverScore;
    }
    /**
     * This method returns the points the client has currently attained.
     * @return The current score of the client. 
     */
    public int getPlayerScore(){
        return playerScore;
    }
    /**
     * Sets the lastPlayed attribute according to the last move registered by
     * the server.
     * @param lastPlayed 
     */
    public void setLastPlayed(int[] lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
    /**
     * Sets the player parameter according to the player currently playing.
     * 1 for the client and 2 for the server.
     * @param player 
     */
    public void setPlayer(int player){
        this.player = player;
    }
    /**
     * Returns the current player.
     * @return The player currently player. Client is 1 and Server is 2.
     */
    public int getPlayer() {
        return player;
    }
    /**
     * Returns the int value at the specific slot denoted by its x and y 
     * coordinates.
     * @param x
     * @param y
     * @return 0 if the slot is empty, 9 if the slot is not playable, 1 if the
     * client has made a move in the slot and 2 if the server has made a move in the slot.
     */
    public int getValueAt(int x, int y) {
//        System.out.println("TEST: Board.getValueAt(): " + x + " " + y);
        int value = gameBoard[y][x]; //may need to swap x,y
        return value;
    }
    
    
}
    