
package three_stones.views;

//import ThreeStones_Game.enums.Turn;
import java.util.ArrayList;
import java.util.List;

public class Board{
//    private Turn turn;
    private int[] lastPlayed;
    private final int stoneCount;
    private int[][] gameBoard;
    private String lastPlayedStr = "";
    private int playerScore;
    private int serverScore;
    private int player;

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
     * Gets any position where the current number is zero
     * @param lastPlayed
     * @return 
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
     * Updates the board and last played string after most recent move
     * 
     * 
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
    
    public int[] getLastPlayed(){
        return lastPlayed;
    }
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
        printScore();
    }
        private int checkScore(int x, int y){
        int totalMoveScore = 0;
        totalMoveScore += checkDiagonalLR(x,y);
        totalMoveScore += checkDiagonalRL(x,y);
        totalMoveScore += checkVertical(x,y);
        totalMoveScore += checkHorizontal(x,y);
        return totalMoveScore;
    }
    
    private int checkDiagonalLR(int x, int y){
        int currentScore = 0;
        if(gameBoard[x-2][y-2]==player&&gameBoard[x-1][y-1]==player){
            currentScore++;
        }
        if(gameBoard[x-1][y-1]==player&&gameBoard[x+1][y+1]==player){
            currentScore++;
        }
        if(gameBoard[x+1][y+1]==player&&gameBoard[y+2][x+2]==player){
            currentScore++;
        }
        return currentScore;
    }
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
    
    public void printScore(){
        System.out.println("Score:\rPlayer: "+playerScore+" Server: "+serverScore);
    }
    
    public int getServerScore(){
        return serverScore;
    }
    
    public int getPlayerScore(){
        return playerScore;
    }
    
    
    
}
    