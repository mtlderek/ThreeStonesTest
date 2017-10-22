
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
        this.gameBoard = gameboard;
        this.stoneCount = 30;
        this.lastPlayed = new int[2];
    }
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
     */
    public void updateBoard(int player, int[] position){
        gameBoard[position[1]][position[0]] = player;
        lastPlayedStr = "Last played move: " + position[0] + ", "+ position[1];
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
        lastPlayed = null;       
        
    }
    
    public List<int[]> getAvailableMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        
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
    
    
    
}
    