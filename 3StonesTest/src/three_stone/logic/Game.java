/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package three_stone.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import three_stones.views.Board;

/**
 *
 * @author derek
 */
public class Game {
    private final int HUMAN = 1;
    private final int ROBOT = 2;
    private Board board;
    private Random random;
    
    public Game(){
        board = new Board();
        random = new Random();
    }
    
    public int[] robotMove(){
        List<int[]> moves = new ArrayList<int[]>();
        moves = getAvailableMoves();
        int selection = random.nextInt(moves.size());
        board.updateBoard(ROBOT, moves.get(selection));
        return moves.get(selection);
    }
    
    public void humanMove(int[] position){
        board.updateBoard(HUMAN, position);
    }
    
    public void printState(){
        System.out.println(board.toString());
    }
    
    public List<int[]> getAvailableMoves(){
        //TODO: determine best moves by points and blockages.
        List<int[]> moves = board.getAvailableMoves();
        List<int[]> movesInAxis = new ArrayList<int[]>();
        int[] lastPlayed = board.getLastPlayed();
      
        for(int[] potentialMove : moves){
            if (potentialMove[0] == lastPlayed[0]){ //check for matching Y
               movesInAxis.add(potentialMove);
            }
            if (potentialMove[1] == lastPlayed[1]){ //check for matchingX
               movesInAxis.add(potentialMove);
            }
        }
        
        if (movesInAxis.isEmpty()) {
            return moves;  
        }
        return movesInAxis;
    }
    
    public boolean isValidMove(int[] move){
        List<int[]> availableMoves = getAvailableMoves();
        for(int[] i : availableMoves){
            System.out.println("" + i[0] + "," + i[1]);
            if( i[0] == move[0] && i[1] == move[1] ){
                return true;
            }
        }
        return false;
    }
    
}
