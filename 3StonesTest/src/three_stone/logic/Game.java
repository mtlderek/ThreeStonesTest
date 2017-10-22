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
    
    
//    public List<int[]> getAvailableMoves(){
//        List<int[]> moves = new ArrayList<int[]>();
//        board
//    }
    public int[] robotMove(){
        List<int[]> moves = new ArrayList<int[]>();
        moves = board.getAvailableMoves();
        int selection = random.nextInt(moves.size());
        board.updateBoard(ROBOT, moves.get(selection));
        System.out.print("\nHERE1: " + moves.get(selection)[0] + " " + moves.get(selection)[1]);
        return moves.get(selection);
        
    }
    
    public void humanMove(int[] position){
        board.updateBoard(HUMAN, position);
    }
    
    public void printState(){
        System.out.println(board.toString());
    }
    
    
}
