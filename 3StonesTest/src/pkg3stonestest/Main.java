/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3stonestest;


import java.util.Arrays;
import java.util.List;
import three_stone.logic.Game;
import three_stones.views.Board;

/**
 *
 * @author derek
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        Board b = new Board();
//        System.out.print(b.toString());
//        b.updateBoard(1, new int[]{7,4});
//        System.out.print(b.toString());
//        b.updateBoard(2, new int[]{7,5});
//        System.out.print(b.toString());
////        System.out.print(b.getAvailableMoves().toString());
//        List<int[]> moves = b.getAvailableMoves();
////        List<int[]> moves = b.getAvailable(b.getLastPlayed());
//        for(int[] move : moves){
//            System.out.print(Arrays.toString(move));
//        }
//        Game game = new Game();
//        game.humanMove(new int[]{7,4});
//        game.printState();
//        game.robotMove();
//        game.printState();
//        game.robotMove();
//        game.printState();
//        game.robotMove();
//        game.printState();
//        game.robotMove();
//        game.printState();
//        game.robotMove();
//        game.printState();
//        game.robotMove();
//        game.printState();
//        byte[] b = new byte[4];
//        int v = 233;
//        b[0] = (byte)2;
//        
//        b[1] = (byte)200;
//        b[2] = (byte)234;
//        b[3] = (byte)43;
//        
//        int move[] = new int[4];
//        for(int j = 0; j < 4; j++){
//            move[j] = b[j] & 0xFF;
//        }
//        
//        System.out.println("" + b);
//        for(int i  = 0 ; i < 4; i++){
//            System.out.println("byte value: " + b[i]);
//            System.out.println("int value: " + move[i]);
//        }
        Board board = new Board();
        System.out.println("value = "+board.getValueAt(3,3));
        
        
        
    }
    //TODO: moves back and forth
    //TODO: AI moves automatically (random for now)
    //TODO: Now same thing but Client/Server version, ASSUME VALID MOVES FOR NOW
    //TODO: design bytes array, what do I need, move identifyier
    
    //TODO: VALIDATION
    //TODO: POINTS
    //TODO: OTHERS
}
